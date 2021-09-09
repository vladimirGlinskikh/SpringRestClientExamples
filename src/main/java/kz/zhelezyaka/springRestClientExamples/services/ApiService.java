package kz.zhelezyaka.springRestClientExamples.services;

import kz.zhelezyaka.api.domain.User;

import java.util.List;

public interface ApiService {
    List<User> getUsers(Integer limit);
}
