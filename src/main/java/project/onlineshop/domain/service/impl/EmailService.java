package project.onlineshop.domain.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import project.onlineshop.domain.model.CartItem;
import project.onlineshop.domain.model.Order;
import project.onlineshop.domain.model.OrderItem;
import project.onlineshop.domain.service.IEmailService;
import project.onlineshop.utils.responses.ItemResponse;
import project.onlineshop.utils.responses.OrderResponse;

@Service
@AllArgsConstructor
public class EmailService implements IEmailService {

    public JavaMailSender emailSender;

    @Override
    public void sendOrderEmail(String toAddress, Order order) {
        String subject = "Order #" + order.getId().toString();
        StringBuilder message = new StringBuilder();
        for(OrderItem item : order.getItems()){
            message.append(item.getItem().getName()).append(":_________").append(item.getItemCount()).append('\n');
        }
        message.append("\n\nThank you for your order))");

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(toAddress);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message.toString());
        emailSender.send(simpleMailMessage);
    }
}
