package jedrekp.preschoolcateringbillcalculator.Controller;


import jedrekp.preschoolcateringbillcalculator.Entity.Diet;
import jedrekp.preschoolcateringbillcalculator.Repository.DietRepository;
import jedrekp.preschoolcateringbillcalculator.Service.DietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@CrossOrigin
public class DietController {

    @Autowired
    DietService dietService;

    @Autowired
    DietRepository dietRepository;

    @PostMapping("/diets")
    public ResponseEntity<Diet> addNewDiet(@RequestBody Diet diet) {
        return new ResponseEntity<>(dietService.save(diet), HttpStatus.OK);
    }

    @GetMapping("/diets")
    public ResponseEntity<Collection<Diet>> getAllDiets() {
        return new ResponseEntity<>(dietService.findAll(), HttpStatus.OK);
    }

}
