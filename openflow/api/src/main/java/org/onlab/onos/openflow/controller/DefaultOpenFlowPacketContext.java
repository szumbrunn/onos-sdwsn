package org.onlab.onos.openflow.controller;


import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

import org.onlab.packet.Ethernet;
import org.projectfloodlight.openflow.protocol.OFPacketIn;
import org.projectfloodlight.openflow.protocol.OFPacketOut;
import org.projectfloodlight.openflow.protocol.action.OFAction;
import org.projectfloodlight.openflow.protocol.action.OFActionOutput;
import org.projectfloodlight.openflow.protocol.match.MatchField;
import org.projectfloodlight.openflow.types.OFBufferId;
import org.projectfloodlight.openflow.types.OFPort;

public final class DefaultOpenFlowPacketContext implements OpenFlowPacketContext {

    private final AtomicBoolean free = new AtomicBoolean(true);
    private final AtomicBoolean isBuilt = new AtomicBoolean(false);
    private final OpenFlowSwitch sw;
    private final OFPacketIn pktin;
    private OFPacketOut pktout = null;

    private final boolean isBuffered;

    private DefaultOpenFlowPacketContext(OpenFlowSwitch s, OFPacketIn pkt) {
        this.sw = s;
        this.pktin = pkt;
        this.isBuffered = pktin.getBufferId() != OFBufferId.NO_BUFFER;
    }

    @Override
    public void send() {
        if (block() && isBuilt.get()) {
            sw.sendMsg(pktout);
        }
    }

    @Override
    public void build(OFPort outPort) {
        if (isBuilt.getAndSet(true)) {
            return;
        }
        OFPacketOut.Builder builder = sw.factory().buildPacketOut();
        OFAction act = buildOutput(outPort.getPortNumber());
        pktout = builder.setXid(pktin.getXid())
                .setInPort(inport())
                .setBufferId(pktin.getBufferId())
                .setActions(Collections.singletonList(act))
                .build();
    }

    @Override
    public void build(Ethernet ethFrame, OFPort outPort) {
        if (isBuilt.getAndSet(true)) {
            return;
        }
        OFPacketOut.Builder builder = sw.factory().buildPacketOut();
        OFAction act = buildOutput(outPort.getPortNumber());
        pktout = builder.setXid(pktin.getXid())
                .setBufferId(OFBufferId.NO_BUFFER)
                .setInPort(inport())
                .setActions(Collections.singletonList(act))
                .setData(ethFrame.serialize())
                .build();
    }

    @Override
    public Ethernet parsed() {
        Ethernet eth = new Ethernet();
        eth.deserialize(pktin.getData(), 0, pktin.getTotalLen());
        return eth;
    }

    @Override
    public Dpid dpid() {
        return new Dpid(sw.getId());
    }

    public static OpenFlowPacketContext packetContextFromPacketIn(OpenFlowSwitch s,
                                                                  OFPacketIn pkt) {
        return new DefaultOpenFlowPacketContext(s, pkt);
    }

    @Override
    public Integer inPort() {
        return inport().getPortNumber();
    }


    private OFPort inport() {
        //FIXME: this has to change in fucking loxi
        try {
            return pktin.getInPort();
        } catch (UnsupportedOperationException e) {
            return pktin.getMatch().get(MatchField.IN_PORT);
        }
    }

    @Override
    public byte[] unparsed() {

        return pktin.getData().clone();

    }

    private OFActionOutput buildOutput(Integer port) {
        OFActionOutput act = sw.factory().actions()
                .buildOutput()
                .setPort(OFPort.of(port))
                .build();
        return act;
    }

    @Override
    public boolean block() {
        return free.getAndSet(false);
    }

    @Override
    public boolean isHandled() {
        return !free.get();
    }

    @Override
    public boolean isBuffered() {
        return isBuffered;
    }

}
