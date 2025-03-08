package com.rayen.gestion_stock.service;


import com.rayen.gestion_stock.dto.LoginRequest;
import com.rayen.gestion_stock.dto.RegisterRequest;
import com.rayen.gestion_stock.dto.Response;
import com.rayen.gestion_stock.dto.UserDTO;
import com.rayen.gestion_stock.entity.User;

public interface UserService {
    Response registerUser(RegisterRequest registerRequest);
    Response loginUser(LoginRequest loginRequest);
    Response getAllUsers();
    User getCurrentLoggedInUser();
    Response updateUser(Long id, UserDTO userDTO);
    Response deleteUser(Long id);
    Response getUserTransactions(Long id);
}
