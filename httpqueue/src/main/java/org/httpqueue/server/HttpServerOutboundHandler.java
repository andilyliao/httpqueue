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
import org.httpqueue.protocolbean.*;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpServerOutboundHandler extends ChannelInboundHandlerAdapter {

    private static Log log = LogFactory.getLog(HttpServerOutboundHandler.class);

    private HttpRequest request;

    private String qnametest="debugtestque";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        Result res=new Result();
        res.setCode(Mode.RESCODE_OK);
        res.setStatus(Mode.RESSTATUS_OK);
        MessageBody body=null;
        String queueName ="testQueue";

            //curl http://localhost:8845/queue -d '{"head":{"qn":"mydirectqueue","id":"uuid","ty":1,"h":0,"o":100,"s":10}}'
            //注册que
            //消费que
        if (msg instanceof HttpRequest) {
            request = (HttpRequest) msg;
            String uri = request.uri();
            queueName = uri.substring(1, uri.length());
            qnametest=queueName;
            log.debug("Queue Name is: " + queueName);
        }
        if (msg instanceof HttpContent) {
            try {
                log.debug("qNametest is: "+qnametest);
                HttpContent content = (HttpContent) msg;
                ByteBuf buf = content.content();
                String message = buf.toString(io.netty.util.CharsetUtil.UTF_8);
                buf.release();
                log.debug("message is: " + message);
                JsonMessage jm = JSON.parseObject(message, JsonMessage.class);
                String head = jm.getHead();
                log.debug("InputHead message is: " + head);
                OutputHead h = JSON.parseObject(head, OutputHead.class);
                int type = h.getTy();
                int hashdisk = h.getH();
                int offset = h.getO();
                int seq = h.getS();
                String clientID = h.getId();
                queueName = h.getQn();
                log.debug("Id is " + clientID + " type is " + type + " hashdisk is: " + hashdisk + " offset is: " + offset + " seq is: " + seq);
                IOutProcessor outProcessor = new OutProcessor();
                body = outProcessor.process(clientID, queueName, h);
                String bodystr = JSON.toJSONString(body);
                log.debug("body: " + bodystr);
                res.setBody(bodystr);
            } catch (Exception e) {
                res.setCode(Mode.RESCODE_SYSTEMERROR);
                res.setStatus(Mode.RESSTATUS_ERROR);
                log.error("system error:", e);
            }
            String result = JSON.toJSONString(res);
            log.debug("result: " + result);
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
                        OK, Unpooled.wrappedBuffer(result.getBytes("UTF-8")));
            log.debug("response: " + response.content().readableBytes());
            response.headers().set(CONTENT_TYPE, "text/plain");
            response.headers().setInt(CONTENT_LENGTH,
                        response.content().readableBytes());
            if (HttpHeaderUtil.isKeepAlive(request)) {
                response.headers().set(CONNECTION, KEEP_ALIVE);
            }
            ctx.write(response);
            ctx.flush();
        }
        log.debug("-------------------------------------------------------");

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