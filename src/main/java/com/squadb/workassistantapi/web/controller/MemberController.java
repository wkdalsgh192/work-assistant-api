package com.squadb.workassistantapi.web.controller;

import com.squadb.workassistantapi.domain.Member;
import com.squadb.workassistantapi.domain.Rental;
import com.squadb.workassistantapi.service.MemberService;
import com.squadb.workassistantapi.service.RentalService;
import com.squadb.workassistantapi.web.controller.dto.LoginRequestDto;
import com.squadb.workassistantapi.web.controller.dto.LoginResponseDto;
import com.squadb.workassistantapi.web.controller.dto.MemberProfileResponseDto;
import com.squadb.workassistantapi.web.controller.dto.RentalResponseDto;
import com.squadb.workassistantapi.web.exception.LoginFailedException;
import com.squadb.workassistantapi.web.interceptor.CheckPermission;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final RentalService rentalService;

    @GetMapping("/auth")
    public ResponseEntity<LoginResponseDto> isLogin(HttpSession session) {
        if (session.getAttribute("MEMBER_ID") == null) {
            return ResponseEntity.ok(LoginResponseDto.fail("UNAUTHORIZED"));
        }
        return ResponseEntity.ok(LoginResponseDto.success((Long) session.getAttribute("MEMBER_ID")));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request, HttpSession session) {
        try {
            long memberId = memberService.login(request.getEmail(), request.getPassword());
            session.setAttribute("MEMBER_ID", memberId);
            return ResponseEntity.ok(LoginResponseDto.success(memberId));
        } catch (LoginFailedException e) {
            return ResponseEntity.ok(LoginResponseDto.fail(e.getResult()));
        }
    }

    @CheckPermission
    @GetMapping("/members/{memberId}/profile")
    public ResponseEntity<MemberProfileResponseDto> getMemberProfile(@PathVariable long memberId) {
        try {
            final Member member = memberService.findById(memberId);
            return new ResponseEntity<>(MemberProfileResponseDto.success(member), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(MemberProfileResponseDto.fail("NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }

    @CheckPermission
    @GetMapping("/members/{memberId}/rentals")
    public ResponseEntity<List<RentalResponseDto>> getMemberBookRentals(@PathVariable long memberId) {
        final List<Rental> rentalList = rentalService.findAllByMember(memberId);
        return new ResponseEntity<>(RentalResponseDto.of(rentalList), HttpStatus.OK);
    }

}
