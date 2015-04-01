package hello.world;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by ִלטענטי on 31.03.2015.
 */
public class UriHandler  extends ServerHandler {

    private String URI_HELLO = "/hello";
    private String URI_STATUS = "/status";
    private String URI_REDIRECT = "/redirect";

    public FullHttpResponse uriCheck(String incomingUrl) throws InterruptedException {

        String url = "";
        if(incomingUrl.contains("%3C")) {

            url = incomingUrl.substring(17, incomingUrl.length()-3);
            incomingUrl = incomingUrl.substring(0, 9);
        //    RequestStatistics.getInstance().putURLandCountHim(url);
        }

        if (incomingUrl.contains(URI_HELLO))
        {
            Thread.sleep(10000);
            //  ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hello World!\r\n", Charset.forName("UTF-8")));
            FullHttpResponse value = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, OK, Unpooled.copiedBuffer("Hello World!\r\n", Charset.forName("UTF-8")));
            return value;
        }
        else if (incomingUrl.contains(URI_STATUS))
        {
            Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Unique connections: " + uniqueConnectionsSum + "\r\n", Charset.forName("UTF-8")));
            Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Overall connections: " + connectionsSum + "\r\n", Charset.forName("UTF-8")));

            for (int i = 0; i < IPs.size(); i++)
            {
                Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(IPs.get(i) + "\r\n", Charset.forName("UTF-8")));
                Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(URIs.get(i) + "\r\n", Charset.forName("UTF-8")));
                Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(timestampList.get(i) + "\r\n", Charset.forName("UTF-8")));
                Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(sent_bytesList.get(i) + "\r\n", Charset.forName("UTF-8")));
                Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(received_bytesList.get(i) + "\r\n", Charset.forName("UTF-8")));
                Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(speedList.get(i) + "\r\n", Charset.forName("UTF-8")));
            }
            //  ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Stats\r\n", Charset.forName("UTF-8")));
            FullHttpResponse value = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, OK, Unpooled.copiedBuffer("Status\r\n", Charset.forName("UTF-8")));
            return value;
        }
        else if (incomingUrl.contains(URI_REDIRECT))
        {

            FullHttpResponse value = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
            value.headers().set(HttpHeaders.Names.LOCATION, url);
            return value;
        }
        else
        {
            DefaultFullHttpResponse value = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, OK, Unpooled.copiedBuffer("Sorry, the page is not found", CharsetUtil.US_ASCII));
            return value;
        }
    }
}

