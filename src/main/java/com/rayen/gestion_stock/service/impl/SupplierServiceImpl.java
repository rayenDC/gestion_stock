package com.rayen.gestion_stock.service.impl;



import com.rayen.gestion_stock.dto.Response;
import com.rayen.gestion_stock.dto.SupplierDTO;
import com.rayen.gestion_stock.entity.Supplier;
import com.rayen.gestion_stock.exceptions.NotFoundException;
import com.rayen.gestion_stock.repository.SupplierRepository;
import com.rayen.gestion_stock.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response addSupplier(SupplierDTO supplierDTO) {
        Supplier supplierToSave = modelMapper.map(supplierDTO, Supplier.class);
        supplierRepository.save(supplierToSave);

        return Response.builder()
                .status(200)
                .message("Supplier added successfully")
                .build();
    }

    @Override
    public Response updateSupplier(Long id, SupplierDTO supplierDTO) {

        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Supplier Not Found"));

        if (supplierDTO.getName() != null) existingSupplier.setName(supplierDTO.getName());
        if (supplierDTO.getAddress() != null) existingSupplier.setAddress(supplierDTO.getAddress());

        supplierRepository.save(existingSupplier);

        return Response.builder()
                .status(200)
                .message("Supplier Successfully Updated")
                .build();
    }

    @Override
    public Response getAllSuppliers() {

        List<Supplier> categories = supplierRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<SupplierDTO> supplierDTOS = modelMapper.map(categories, new TypeToken<List<SupplierDTO>>() {}.getType());

        return Response.builder()
                .status(200)
                .message("success")
                .suppliers(supplierDTOS)
                .build();
    }

    @Override
    public Response getSupplierById(Long id) {

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Supplier Not Found"));

        SupplierDTO supplierDTO = modelMapper.map(supplier, SupplierDTO.class);

        return Response.builder()
                .status(200)
                .message("success")
                .supplier(supplierDTO)
                .build();
    }

    @Override
    public Response deleteSupplier(Long id) {

        supplierRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Supplier Not Found"));

        supplierRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("Supplier Successfully Deleted")
                .build();
    }
}
