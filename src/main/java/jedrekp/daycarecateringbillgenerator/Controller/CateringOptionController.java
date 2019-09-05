package jedrekp.daycarecateringbillgenerator.Controller;


import jedrekp.daycarecateringbillgenerator.Entity.CateringOption;
import jedrekp.daycarecateringbillgenerator.Repository.CateringOptionRepository;
import jedrekp.daycarecateringbillgenerator.Service.CateringOptionService;
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
    public ResponseEntity<CateringOption> addNewCateringOption(@RequestBody CateringOption cateringOption) {
        return new ResponseEntity<>(cateringOptionService.save(cateringOption), HttpStatus.OK);
    }

    @PutMapping("/cateringOptions/{cateringOptionId}")
    public ResponseEntity<CateringOption> editCateringOption(@PathVariable Long cateringOptionId,
                                                             @RequestBody CateringOption cateringOption) {
        return new ResponseEntity<>(
                cateringOptionService.editCateringOption(cateringOption, cateringOptionId), HttpStatus.OK);
    }

    @GetMapping("/cateringOptions")
    public ResponseEntity<Collection<CateringOption>> getAllCateringOptions() {
        return new ResponseEntity<>(cateringOptionService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/cateringOptions", params = "disabled")
    public ResponseEntity<Collection<CateringOption>> getAllCateringOptionsByDisabled(@RequestParam boolean disabled) {
        return new ResponseEntity<>(cateringOptionService.findAllByDisabled(disabled), HttpStatus.OK);
    }
}
