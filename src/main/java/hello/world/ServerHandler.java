package hello.world;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.util.HashedWheelTimer;
//import io.netty.util.Timer;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;


/**
 * Created by ִלטענטי on 30.03.2015.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private String ip;
    protected String incomingURI = "";

    protected int sent_bytes;
    protected int recieved_bytes;
    protected double speed;
    protected String timestamp;
    protected int connectionsSum;
    protected int uniqueConnectionsSum;

    protected ArrayList<String> URIs = new ArrayList<String>();
    protected ArrayList<String> IPs = new ArrayList<String>();
    protected ArrayList<String> uniqueIPs = new ArrayList<String>();
    protected ArrayList<String> timestampList = new ArrayList<String>();
    protected ArrayList<Integer> sent_bytesList = new ArrayList<Integer>();
    protected ArrayList<Integer> received_bytesList = new ArrayList<Integer>();
    protected ArrayList<Double> speedList = new ArrayList<Double>();

    ServerHandler() {}

    ServerHandler(String ip) {
        this.ip = ip;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws InterruptedException {
        ByteBuf in = (ByteBuf) msg;
        Channel incoming = ctx.channel();

        incomingURI = ((HttpRequest) msg).getUri().toLowerCase();
        FullHttpResponse response = new UriHandler().uriCheck((incomingURI));
    //    HttpRequest request = (HttpRequest) msg;
     //   incomingURI = request.getUri();



       System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));

              // Thread.sleep(5000);
      //  ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hello World!\r\n", Charset.forName("UTF-8")));
      //  ctx.write(buf.duplicate()).addListener(ChannelFutureListener.CLOSE);

      //  System.out.println("remoteIP         " + incoming.remoteAddress());
    //    ctx.write(msg);

        //////statistics
        recieved_bytes += msg.toString().length();
        sent_bytes = response.content().writerIndex();
        speed = (recieved_bytes + sent_bytes) / (System.currentTimeMillis() / 1000);

        received_bytesList.add(recieved_bytes);
        sent_bytesList.add(sent_bytes);
        speedList.add(speed);

        URIs.add(incomingURI);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        timestamp = sdf.format(cal.getTime());
        timestampList.add(timestamp);

        IPs.add(String.valueOf(incoming.remoteAddress()));

        for (int i = 0; i < IPs.size(); i++)
        {
            if (String.valueOf(incoming.remoteAddress()).equals(IPs.get(i)))
                continue;
            else
                uniqueIPs.add(String.valueOf(incoming.remoteAddress()));
        }

        connectionsSum = IPs.size();
        uniqueConnectionsSum = uniqueIPs.size();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();

    }
}
