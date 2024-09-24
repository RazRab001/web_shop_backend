package project.onlineshop.domain.service;

import project.onlineshop.domain.model.Order;
import project.onlineshop.utils.responses.OrderResponse;

public interface IEmailService {
    void sendOrderEmail(String toAddress, Order order);
}
