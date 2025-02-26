package org.example.dto.address;

import lombok.Data;
import org.example.enums.AddressType;

@Data
public class AddressResponse {
    private Long id;
    private String title;
    private String addressLine;
    private String city;
    private String state;
    private AddressType addressType;
    private boolean isDefault;
}