package org.httpqueue.server;


import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.httpqueue.protocolbean.Mode;
import org.httpqueue.protocolbean.Result;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpServerManagerHandler extends ChannelInboundHandlerAdapter {

    private static Log log = LogFactory.getLog(HttpServerManagerHandler.class);

    private HttpRequest request;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        Result res = new Result();
        res.setCode(Mode.RESCODE_OK);
        res.setStatus(Mode.RESSTATUS_OK);
        String body = "";
        String queueName = "testQueue";

        //curl -post http://localhost:8845/queue -d '{"head":{"ty":1,"h":0,"o":100,"s":10}}'
        if (msg instanceof HttpRequest) {
            request = (HttpRequest) msg;
            String uri = request.uri();
            queueName = uri.substring(1, uri.length());
            log.debug("Queue Name is: " + queueName);
        }
        if (msg instanceof HttpContent) {
            try {
                HttpContent content = (HttpContent) msg;
                ByteBuf buf = content.content();
                String message = buf.toString(io.netty.util.CharsetUtil.UTF_8);
                buf.release();
                log.debug("message is: " + message);

            } catch (Exception e) {
                res.setCode(Mode.RESCODE_SYSTEMERROR);
                res.setStatus(Mode.RESSTATUS_ERROR);
                log.error("system error:", e);
            }
            String result = JSON.toJSONString(res);
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