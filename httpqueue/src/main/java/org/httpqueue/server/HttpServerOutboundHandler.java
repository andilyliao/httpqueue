package org.httpqueue.server;


import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.httpqueue.outprocess.OutProcessor;
import org.httpqueue.outprocess.intf.IOutProcessor;
import org.httpqueue.protocolbean.JsonMessage;
import org.httpqueue.protocolbean.Mode;
import org.httpqueue.protocolbean.OutputHead;
import org.httpqueue.protocolbean.Result;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpServerOutboundHandler extends ChannelInboundHandlerAdapter {

    private static Log log = LogFactory.getLog(HttpServerOutboundHandler.class);

    private HttpRequest request;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        Result res=new Result();
        res.setCode(Mode.RESCODE_OK);
        res.setStatus(Mode.RESSTATUS_OK);
        String body="";
        String queueName ="testQueue";
        try {
            //curl -post http://localhost:8845/queue -d '{"head":{"ty":1,"h":0,"o":100,"s":10}}'
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
                log.debug("InputHead message is: " + head);
                OutputHead h = JSON.parseObject(head, OutputHead.class);
                int type=h.getTy();
                int hashdisk = h.getH();
                int offset=h.getO();
                int seq = h.getS();
                log.debug("Type is "+type+ " hashdisk is: " + hashdisk + " offset is: "+offset+" seq is: " + seq);
                IOutProcessor outProcessor=new OutProcessor();
                body=outProcessor.process(queueName,h);
                res.setBody(body);
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