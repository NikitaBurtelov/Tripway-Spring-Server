package tripway.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tripway.server.service.trips.TripService;

/**
 * @author Nikita Burtelov
 */
@Controller
@RequestMapping("/api/v1")
public class TripController {
    private TripService tripService;

    @GetMapping("/trip")
    public String getTripId() {
        return null;
    }

    @PostMapping("/trip")
    public String saveTrip() {
        return null;
    }

    @DeleteMapping("/trip")
    public String removeTrip() {
        return null;
    }

    @PatchMapping("trip")
    public String updateTrip() {
        return null;
    }
}
