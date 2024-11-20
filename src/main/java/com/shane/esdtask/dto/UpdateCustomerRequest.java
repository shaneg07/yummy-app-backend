package com.shane.esdtask.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateCustomerRequest(
        @NotBlank(message = "Name cannot be empty")
        String f_name,
        String l_name

) {}
