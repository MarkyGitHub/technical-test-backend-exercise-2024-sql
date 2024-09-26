package com.playtomic.tests.wallet.api;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.service.WalletService;

@WebMvcTest(WalletController.class)
public class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean 
    private WalletService walletService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetWalletById() throws Exception {
      
        Long walletId = 1L;
        Wallet wallet = new Wallet(BigDecimal.valueOf(100.00));
        wallet.setId(walletId);

        when(walletService.getWalletById(walletId)).thenReturn(wallet);

       
        mockMvc.perform(get("/{id}", walletId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(walletId))
                .andExpect(jsonPath("$.balance").value(100.00));

        verify(walletService, times(1)).getWalletById(walletId);
    }

    @Test
    public void testTopUpWallet() throws Exception {
        
        Long walletId = 1L;
        BigDecimal topUpAmount = BigDecimal.valueOf(50.00);
        String creditCardNumber = "4111111111111111";

        Wallet wallet = new Wallet(BigDecimal.valueOf(150.00)); // Updated balance
        wallet.setId(walletId);

        when(walletService.topUpWallet(walletId, topUpAmount, creditCardNumber)).thenReturn(wallet);

       
        mockMvc.perform(post("/{id}/topup", walletId)
                .param("amount", topUpAmount.toString())
                .param("creditCardNumber", creditCardNumber)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(walletId))
                .andExpect(jsonPath("$.balance").value(150.00));

        verify(walletService, times(1)).topUpWallet(walletId, topUpAmount, creditCardNumber);
    }

    @Test
    public void testLogEndpoint() throws Exception {
      
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
        
    }
}
