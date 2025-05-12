package com.rayen.gestion_stock.service.impl;

import com.rayen.gestion_stock.dto.ClientDTO;
import com.rayen.gestion_stock.dto.Response;
import com.rayen.gestion_stock.dto.TransactionDTO;
import com.rayen.gestion_stock.dto.TransactionRequest;
import com.rayen.gestion_stock.entity.Client;
import com.rayen.gestion_stock.entity.Product;
import com.rayen.gestion_stock.entity.Supplier;
import com.rayen.gestion_stock.entity.Transaction;
import com.rayen.gestion_stock.entity.User;
import com.rayen.gestion_stock.enums.TransactionStatus;
import com.rayen.gestion_stock.enums.TransactionType;
import com.rayen.gestion_stock.exceptions.NameValueRequiredException;
import com.rayen.gestion_stock.exceptions.NotFoundException;
import com.rayen.gestion_stock.repository.ClientRepository;
import com.rayen.gestion_stock.repository.ProductRepository;
import com.rayen.gestion_stock.repository.SupplierRepository;
import com.rayen.gestion_stock.repository.TransactionRepository;
import com.rayen.gestion_stock.service.TransactionService;
import com.rayen.gestion_stock.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final ClientRepository clientRepository; // ✅ NEW
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Override
    public Response restockInventory(TransactionRequest transactionRequest) {
        Long productId = transactionRequest.getProductId();
        Long supplierId = transactionRequest.getSupplierId();
        Integer quantity = transactionRequest.getQuantity();

        if (supplierId == null) throw new NameValueRequiredException("Supplier Id is Required");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new NotFoundException("Supplier Not Found"));

        User user = userService.getCurrentLoggedInUser();

        product.setStockQuantity(product.getStockQuantity() + quantity);
        productRepository.save(product);

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.PURCHASE)
                .status(TransactionStatus.COMPLETED)
                .product(product)
                .user(user)
                .supplier(supplier)
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .description(transactionRequest.getDescription())
                .build();

        transactionRepository.save(transaction);

        return Response.builder()
                .status(200)
                .message("Transaction Made Successfully")
                .build();
    }

    @Override
    public Response sell(TransactionRequest transactionRequest) {
        Long productId = transactionRequest.getProductId();
        Integer quantity = transactionRequest.getQuantity();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        User user = userService.getCurrentLoggedInUser();

        // ✅ Fetch client if provided
        Client client = null;
        if (transactionRequest.getClientId() != null) {
            client = clientRepository.findById(transactionRequest.getClientId())
                    .orElseThrow(() -> new NotFoundException("Client not found"));
        }

        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.SALE)
                .status(TransactionStatus.COMPLETED)
                .product(product)
                .user(user)
                .client(client) // ✅ ADD CLIENT
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .description(transactionRequest.getDescription())
                .build();

        transactionRepository.save(transaction);

        return Response.builder()
                .status(200)
                .message("Transaction Sold Successfully")
                .build();
    }

    @Override
    public Response returnToSupplier(TransactionRequest transactionRequest) {
        Long productId = transactionRequest.getProductId();
        Long supplierId = transactionRequest.getSupplierId();
        Integer quantity = transactionRequest.getQuantity();

        if (supplierId == null) throw new NameValueRequiredException("Supplier Id is Required");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new NotFoundException("Supplier Not Found"));

        User user = userService.getCurrentLoggedInUser();

        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.RETURN_TO_SUPPLIER)
                .status(TransactionStatus.PROCESSING)
                .product(product)
                .user(user)
                .supplier(supplier)
                .totalProducts(quantity)
                .totalPrice(BigDecimal.ZERO)
                .description(transactionRequest.getDescription())
                .build();

        transactionRepository.save(transaction);

        return Response.builder()
                .status(200)
                .message("Transaction Returned Successfully Initialized")
                .build();
    }

    @Override
    public Response getAllTransactions(int page, int size, String searchText) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Transaction> transactionPage = transactionRepository.searchTransactions(searchText, pageable);

        List<TransactionDTO> transactionDTOS = modelMapper
                .map(transactionPage.getContent(), new TypeToken<List<TransactionDTO>>() {}.getType());

        transactionDTOS.forEach(transactionDTOItem -> {
            transactionDTOItem.setUser(null);
            transactionDTOItem.setProduct(null);
            transactionDTOItem.setSupplier(null);
        });

        return Response.builder()
                .status(200)
                .message("success")
                .transactions(transactionDTOS)
                .build();
    }

    @Override
    public Response getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction Not Found"));

        TransactionDTO transactionDTO = modelMapper.map(transaction, TransactionDTO.class);

        // Clean nested user data
        if (transactionDTO.getUser() != null) {
            transactionDTO.getUser().setTransactions(null);
        }

        // 🚨 Add this to ensure client is set even if modelMapper skips it
        if (transaction.getClient() != null) {
            ClientDTO clientDTO = modelMapper.map(transaction.getClient(), ClientDTO.class);
            transactionDTO.setClient(clientDTO);
        }

        return Response.builder()
                .status(200)
                .message("success")
                .transaction(transactionDTO)
                .build();
    }


    @Override
    public Response getAllTransactionByMonthAndYear(int month, int year) {
        List<Transaction> transactions = transactionRepository.findAllByMonthAndYear(month, year);

        List<TransactionDTO> transactionDTOS = modelMapper
                .map(transactions, new TypeToken<List<TransactionDTO>>() {}.getType());

        transactionDTOS.forEach(transactionDTOItem -> {
            transactionDTOItem.setUser(null);
            transactionDTOItem.setProduct(null);
            transactionDTOItem.setSupplier(null);
        });

        return Response.builder()
                .status(200)
                .message("success")
                .transactions(transactionDTOS)
                .build();
    }

    @Override
    public Response updateTransactionStatus(Long transactionId, TransactionStatus transactionStatus) {
        Transaction existingTransaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("Transaction Not Found"));

        existingTransaction.setStatus(transactionStatus);
        existingTransaction.setUpdatedAt(LocalDateTime.now());

        transactionRepository.save(existingTransaction);

        return Response.builder()
                .status(200)
                .message("Transaction Status Successfully Updated")
                .build();
    }
}
