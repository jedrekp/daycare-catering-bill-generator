package jedrekp.preschoolcateringbillcalculator.Controller;


import jedrekp.preschoolcateringbillcalculator.Entity.CateringOption;
import jedrekp.preschoolcateringbillcalculator.Repository.CateringOptionRepository;
import jedrekp.preschoolcateringbillcalculator.Service.CateringOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@CrossOrigin
public class CateringOptionController {

    @Autowired
    CateringOptionService cateringOptionService;

    @Autowired
    CateringOptionRepository cateringOptionRepository;

    @PostMapping("/cateringOptions")
    public ResponseEntity<CateringOption> addNewDiet(@RequestBody CateringOption cateringOption) {
        return new ResponseEntity<>(cateringOptionService.save(cateringOption), HttpStatus.OK);
    }

    @GetMapping("/cateringOptions")
    public ResponseEntity<Collection<CateringOption>> getAllDiets() {
        return new ResponseEntity<>(cateringOptionService.findAll(), HttpStatus.OK);
    }

}
