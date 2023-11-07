package com.example.library.service.impl;

import com.example.library.dto.CartItemDto;
import com.example.library.dto.ProductDto;
import com.example.library.dto.ShoppingCartDto;
import com.example.library.model.CartItem;
import com.example.library.model.Customer;
import com.example.library.model.Product;
import com.example.library.model.ShoppingCart;
import com.example.library.repository.CartItemRepository;
import com.example.library.repository.ShoppingCartRepository;
import com.example.library.service.CustomerService;
import com.example.library.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final CartItemRepository itemRepository;

    private final ShoppingCartRepository cartRepository;
    private final CustomerService customerService;
    @Override
    public ShoppingCart addItemToCart(ProductDto productDto, int quantity,String username) {
        Customer customer = customerService.findByUsername(username);
        ShoppingCart cart = customer.getCart();
        if (cart == null){
            cart = new ShoppingCart();
        }
        Set<CartItem> cartItems = cart.getCartItems();
        CartItem cartItem = find(cartItems, productDto.getId());
        Product product = transferData(productDto);
        double unitPrice = productDto.getCostPrice();
        int itemQuantity = 0;
        if (cartItems == null){
            cartItems = new HashSet<>();
            if (cartItem == null){
                cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setCart(cart);
                cartItem.setQuantity(quantity);
                cartItem.setUnitPrice(unitPrice);
                cartItem.setCart(cart);
                cartItems.add(cartItem);
                itemRepository.save(cartItem);
            }else {
                itemQuantity = cartItem.getQuantity() + quantity;
                cartItem.setQuantity(itemQuantity);
                itemRepository.save(cartItem);
            }
        }else {
            if (cartItem == null){
                cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setCart(cart);
                cartItem.setQuantity(quantity);
                cartItem.setUnitPrice(unitPrice);
                cartItem.setCart(cart);
                cartItems.add(cartItem);
                itemRepository.save(cartItem);
            }else {
                itemQuantity = cartItem.getQuantity() + quantity;
                cartItem.setQuantity(itemQuantity);
                itemRepository.save(cartItem);
            }

        }
        cart.setCartItems(cartItems);
        int totalItem = totalItem(cart.getCartItems());
        double totalPrice = totalPrice(cart.getCartItems());
        cart.setTotalPrices(totalPrice);
        cart.setTotalItems(totalItem);
        cart.setCustomer(customer);
        return cartRepository.save(cart);
    }

    @Override
    public ShoppingCart updateCart(ProductDto productDto, int quantity, String username) {
        Customer customer = customerService.findByUsername(username);
        ShoppingCart cart = customer.getCart();
        Set<CartItem> cartItems = cart.getCartItems();
        CartItem item = find(cartItems, productDto.getId());
        int itemQuantity = quantity;
        item.setQuantity(itemQuantity);
        itemRepository.save(item);
        cart.setCartItems(cartItems);
        int totalItem = totalItem(cartItems);
        double totalPrice = totalPrice(cartItems);
        cart.setTotalItems(totalItem);
        cart.setTotalPrices(totalPrice);
        return cartRepository.save(cart);
    }

    @Override
    public ShoppingCart removeItemFromCart(ProductDto productDto, String username) {
        Customer customer = customerService.findByUsername(username);
        ShoppingCart cart = customer.getCart();
        Set<CartItem> cartItems = cart.getCartItems();
        CartItem item = find(cartItems, productDto.getId());
        cartItems.remove(item);
        itemRepository.delete(item);
        double totalPrice = totalPrice(cartItems);
        int totalItems = totalItem(cartItems);
        cart.setCartItems(cartItems);
        cart.setTotalItems(totalItems);
        cart.setTotalPrices(totalPrice);
        return cartRepository.save(cart);
    }

    @Override
    public void deleteCartById(Long id) {
        ShoppingCart shoppingCart = cartRepository.getReferenceById(id);
        for (CartItem cartItem : shoppingCart.getCartItems()) {
            itemRepository.deleteById(cartItem.getId());
        }
        shoppingCart.setCustomer(null);
        shoppingCart.getCartItems().clear();
        shoppingCart.setTotalPrices(0);
        shoppingCart.setTotalItems(0);
        cartRepository.save(shoppingCart);
    }

    private int totalItem(Set<CartItem> cartItems) {
        int totalItems = 0;
        for (CartItem item : cartItems) {
            totalItems +=item.getQuantity();
        }
        return totalItems;
    }
    private double totalPrice(Set<CartItem> cartItems){
        double totalPrice = 0.0;
        for (CartItem item  : cartItems) {
            totalPrice += item.getUnitPrice()*item.getQuantity();
        }
        return totalPrice;
    }

    private CartItem find(Set<CartItem> cartItems, Long productId){
        if (cartItems == null){
            return null;
        }
        CartItem cartItem = null;
        for (CartItem item : cartItems){
            if (item.getProduct().getId() == productId ){
                cartItem = item;
            }
        }
        return cartItem;
    }
    private CartItemDto findInDto(ShoppingCartDto shoppingCartDto, long productId){
        if (shoppingCartDto == null){
            return null;
        }
        CartItemDto cartItemDto = null;
        for (CartItemDto item : shoppingCartDto.getCartItems()) {
            if (item.getProduct().getId() == productId){
                cartItemDto = item;
            }
        }
        return cartItemDto;
    }
    private int totalItemDto(Set<CartItemDto> cartItemDtos){
        int totalItem = 0;
        for (CartItemDto item : cartItemDtos) {
            totalItem += item.getQuantity();
        }
        return totalItem;
    }
    private double totalPriceDto(Set<CartItemDto> cartItemDtos){
        double totalPrice = 0;
        for (CartItemDto item : cartItemDtos) {
            totalPrice += item.getUnitPrice() * item.getQuantity();
        }
        return totalPrice;
    }
    private Product transferData(ProductDto productDto){
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setCurrentQuantity(productDto.getCurrentQuantity());
        product.setCostPrice(productDto.getCostPrice());
        product.setSalePrice(productDto.getSalePrice());
        product.setDescription(productDto.getDescription());
        product.setImage(productDto.getImage());
        product.setCategory(productDto.getCategory());
        product.set_activated(productDto.isActivated());
        product.set_deleted(productDto.isDeleted());
        return product;
    }
    private Set<CartItem> convertCartItem(Set<CartItemDto> cartItemDtos, ShoppingCart cart){
        Set<CartItem> cartItems = new HashSet<>();
        for (CartItemDto cartitemDto : cartItemDtos) {
            CartItem cartItem = new CartItem();
            cartItem.setQuantity(cartitemDto.getQuantity());
            cartItem.setProduct(transferData(cartitemDto.getProduct()));
            cartItem.setUnitPrice(cartitemDto.getUnitPrice());
            cartItem.setId(cartitemDto.getId());
            cartItem.setCart(cart);
            cartItems.add(cartItem);

        }
        return  cartItems;
    }
}
