package com.github.homework.program.controller;

import com.github.homework.program.exception.ProgramNotFoundException;
import com.github.homework.program.model.ProgramSaveDto;
import com.github.homework.program.model.ProgramViewDetailDto;
import com.github.homework.program.model.ProgramViewDto;
import com.github.homework.program.model.SimpleResponse;
import com.github.homework.program.service.ProgramSaveService;
import com.github.homework.program.service.ProgramViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/programs")
@RequiredArgsConstructor
public class ProgramController {

    private final ProgramViewService programViewService;
    private final ProgramSaveService programSaveService;


    @GetMapping
    public ResponseEntity<Page<ProgramViewDto>> pageBy(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(this.programViewService.pageBy(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramViewDetailDto> getBy(@PathVariable Long id) {
        Optional<ProgramViewDetailDto> programViewDto = this.programViewService.getBy(id);
        return programViewDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{name}")
    // name을 value로 받는다.
    public ResponseEntity<ProgramViewDetailDto> getByName(@PathVariable String name) {
        Optional<ProgramViewDetailDto> programViewDto = this.programViewService.getByName(name);
        return programViewDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    // @Valid 어노테이션을 사용하는 이유는 사용자가 넘겨주는 값은 검증을 거쳐야한다.
    // 따라서 검증을해주는 코드이다.
    // 프론트사이드에서도 검증해주어야 하지만, 서버사이드에서도 검증을 진행해야 한다.
    public ResponseEntity<SimpleResponse> saveProgram(@RequestBody @Valid ProgramSaveDto programSaveDto) {
        this.programSaveService.saveProgram(programSaveDto);
        return ResponseEntity.ok(new SimpleResponse(true, "저장 성공"));
    }

    @PutMapping
    public ResponseEntity<SimpleResponse> updateProgram(@RequestBody @Valid ProgramSaveDto programSaveDto) {
        try {
            this.programSaveService.updateProgram(programSaveDto);
        } catch (ProgramNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new SimpleResponse(true, "수정 성공"));
    }
}
