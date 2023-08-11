package charger.socket.netty;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.util.List;

import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
@Component
public class TCPServer {
    
    private final ServerBootstrap serverBootstrap;
    
    private final InetSocketAddress tcpPort;
    
    private Channel serverChannel;

    public void start() {
        try {
    		ChannelFuture serverChannelFuture = serverBootstrap.bind(tcpPort).sync();
    		serverChannel = serverChannelFuture.channel().closeFuture().sync().channel();        		
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    

    @PreDestroy
    public void stop() {
        if( serverChannel != null ) {
            serverChannel.close();
            serverChannel.parent().close();
        }
    }
}
