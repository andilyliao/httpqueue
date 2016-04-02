package org.httpqueue.server;


import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.apache.log4j.Logger;
import org.httpqueue.inprocess.intf.IInProcessor;
import org.httpqueue.inprocess.InProcessor;
import org.httpqueue.protocolbean.InputHead;
import org.httpqueue.protocolbean.JsonMessage;
import org.httpqueue.protocolbean.Mode;
import org.httpqueue.protocolbean.Result;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpServerInboundHandler extends ChannelInboundHandlerAdapter {

    private static Logger log = Logger.getLogger(HttpServerInboundHandler.class);

    private HttpRequest request;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        Result res=new Result();
        res.setCode(Mode.RESCODE_OK);
        res.setStatus(Mode.RESSTATUS_OK);
        String queueName ="testQueue";
        try {
            //curl -post http://localhost:8844/queue -d '{"head":{"ty":1,"m":0,"t":100,"d":0,"tr":0,"s":0},"body":{"aaa":"bbb","ccc":"ddd"}}'
            //curl -post http://localhost:8844/queue -d '{"head":{"ty":1,"m":0,"t":100,"d":0},"body":{"aaa":"bbb","ccc":"ddd"}}'
            if (msg instanceof HttpRequest) {
                request = (HttpRequest) msg;
                String uri = request.uri();
                queueName = uri.substring(1, uri.length());
                log.debug("Queue Name is: " + queueName);
            }
            if (msg instanceof HttpContent) {
                HttpContent content = (HttpContent) msg;
                ByteBuf buf = content.content();
                String message = buf.toString(io.netty.util.CharsetUtil.UTF_8);
                buf.release();
                log.debug("message is: " + message);
                JsonMessage jm = JSON.parseObject(message, JsonMessage.class);
                String head = jm.getHead();
                String body = jm.getBody();
                log.debug("InputHead message is: " + head);
                log.debug("MessageBody message is: " + body);
                InputHead h = JSON.parseObject(head, InputHead.class);
                int type=h.getTy();
                int mode = h.getM();
                int ttl = h.getT();
                int hashdisk = h.getH();
                int hastransaction = h.getTr();
                int seq = h.getS();
                log.debug("Type is "+type+"mode is: " + mode + " ttl is: " + ttl + " hashdisk is: " + hashdisk + " hastransaction is: "+hastransaction+" seq is: " + seq);
                IInProcessor inProcessor=new InProcessor();
                inProcessor.process(queueName,h,body);
            }
        }catch (Exception e){
            res.setCode(Mode.RESCODE_SYSTEMERROR);
            res.setStatus(Mode.RESSTATUS_ERROR);
            log.error("system error:",e);
        }
        String result=JSON.toJSONString(res);
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
                OK, Unpooled.wrappedBuffer(result.getBytes("UTF-8")));
        response.headers().set(CONTENT_TYPE, "text/plain");
        response.headers().setInt(CONTENT_LENGTH,
                response.content().readableBytes());
        if (HttpHeaderUtil.isKeepAlive(request)) {
            response.headers().set(CONNECTION, KEEP_ALIVE);
        }
        ctx.write(response);
        ctx.flush();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause.getMessage());
        ctx.close();
    }

}