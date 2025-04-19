package dev.zrdzn.hiresynapse.hiresynapsebackend.controller;

import dev.zrdzn.hiresynapse.hiresynapsebackend.service.ReportService;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/stats")
public class StatController {

    private final UserService userService;
    private final ReportService reportService;

    public StatController(UserService userService, ReportService reportService) {
        this.userService = userService;
        this.reportService = reportService;
    }

    @GetMapping("/user-count")
    public int getUserCount() {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/report-count")
    public int getReportCount() {
        throw new UnsupportedOperationException();
    }



}
