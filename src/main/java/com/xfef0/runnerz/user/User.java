package com.xfef0.runnerz.user;

import org.springframework.data.annotation.Id;

public record User(
        @Id
        Integer id,
        String name,
        String username,
        String email,
        Address address,
        String phone,
        String website,
        Company company
) {
}
