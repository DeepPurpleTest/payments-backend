package com.example.payments.controller.client;

import com.example.monitoringservice.entity.MethodExecutionTimeEvent;
import com.example.monitoringservice.service.TimeCheckerService;
import com.example.payments.configuration.securityconfig.PersonDetails;
import com.example.payments.dto.CardDto;
import com.example.payments.dto.InPaymentDto;
import com.example.payments.dto.OutPaymentDto;
import com.example.payments.entity.Card;
import com.example.payments.entity.Payment;
import com.example.payments.service.PaymentService;
import com.example.payments.util.exception.EntityValidationException;
import com.example.payments.util.mapper.GenericMapper;
import com.example.payments.util.mapper.PaymentOutPaymentDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client/payment")
@RequiredArgsConstructor
@Slf4j
public class ClientPaymentController {
    private final PaymentService paymentService;
    private final TimeCheckerService timeCheckerService;
    private final GenericMapper<Payment, InPaymentDto> inPaymentMapper;
    private final GenericMapper<Card, CardDto> cardMapper;
    private final PaymentOutPaymentDtoMapper outPaymentMapper;

    @GetMapping("/all")
    public List<OutPaymentDto> findAll(@AuthenticationPrincipal PersonDetails personDetails) {
        List<Payment> payments = paymentService.findByCurrentUser(personDetails.getUser());
        return payments.stream()
                .map(payment -> outPaymentMapper.toDto(payment, personDetails.getUser()))
                .toList();
    }

    @GetMapping("/find/{id}")
    public OutPaymentDto findOne(@AuthenticationPrincipal PersonDetails personDetails,
                                 @PathVariable("id") Long id) {
        Payment payment = paymentService.findById(id);
        return outPaymentMapper.toDto(payment, personDetails.getUser());
    }

    @PostMapping("/_findAll")
    public List<OutPaymentDto> findAllByCardNumber(@RequestBody @Valid CardDto dto,
                                                   BindingResult bindingResult,
                                                   @AuthenticationPrincipal PersonDetails personDetails) {
        if (bindingResult.hasErrors()) {
            throw new EntityValidationException("Card validation error", bindingResult);
        }

        List<Payment> payments = paymentService.findByCardNumber(cardMapper.toEntity(dto));
        return payments.stream()
                .map(payment -> outPaymentMapper.toDto(payment, personDetails.getUser()))
                .toList();
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public OutPaymentDto createTransaction(@RequestBody @Valid InPaymentDto dto,
                                           BindingResult bindingResult,
                                           @AuthenticationPrincipal PersonDetails personDetails) {
        if (bindingResult.hasErrors()) {
            throw new EntityValidationException("Cannot create transaction", bindingResult);
        }
        Long startTime = System.currentTimeMillis();

        Payment paymentToCreate = inPaymentMapper.toEntity(dto);
        Payment createdPayment = paymentService.create(paymentToCreate, personDetails.getUser());

        Long endTime = System.currentTimeMillis();

        MethodExecutionTimeEvent event = MethodExecutionTimeEvent.builder()
                .methodName(log.getName())
                .executionTime(endTime - startTime)
                .build();
        timeCheckerService.saveEvent(event);
        return outPaymentMapper.toDto(createdPayment, personDetails.getUser());
    }
}
