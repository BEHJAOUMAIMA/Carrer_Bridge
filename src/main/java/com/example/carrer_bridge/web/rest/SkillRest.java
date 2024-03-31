package com.example.carrer_bridge.web.rest;


import com.example.carrer_bridge.domain.entities.Skill;
import com.example.carrer_bridge.domain.entities.User;
import com.example.carrer_bridge.dto.request.UserSkillsRequest;
import com.example.carrer_bridge.dto.response.UserSkillsResponse;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.handler.response.ResponseMessage;
import com.example.carrer_bridge.mappers.UserSkillsMapper;
import com.example.carrer_bridge.service.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/skills")
public class SkillRest {
    private final SkillService skillService;
    private final UserSkillsMapper userSkillsMapper;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('VIEW_SKILL')")
    public ResponseEntity<List<UserSkillsResponse>> getAllSkills() {
        List<Skill> skills = skillService.findAll();
        List<UserSkillsResponse> SkillResponseDtos = skills.stream().map(userSkillsMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(SkillResponseDtos);
    }

    @GetMapping("/{skillId}")
    @PreAuthorize("hasAnyAuthority('VIEW_SKILL')")

    public ResponseEntity<?> getSkillById(@PathVariable Long skillId) {
        Skill Skill = skillService.findById(skillId)
                .orElseThrow(() -> new OperationException("Skill not found with ID: " + skillId));
        UserSkillsResponse SkillResponseDto = userSkillsMapper.toResponseDto(Skill);
        return ResponseEntity.ok(SkillResponseDto);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('CREATE_SKILL')")

    public ResponseEntity<ResponseMessage> addSkill(@Valid @RequestBody UserSkillsRequest skillRequestDto) {
        skillService.save(userSkillsMapper.fromRequestDto(skillRequestDto));
        return ResponseMessage.created("Skill created successfully", null);
    }

    @PutMapping("/update/{skillId}")
    @PreAuthorize("hasAnyAuthority('UPDATE_SKILL')")
    public ResponseEntity<ResponseMessage> updateSkill(@PathVariable Long skillId, @Valid @RequestBody UserSkillsRequest skillRequestDto) {
        Skill updatedSkill = userSkillsMapper.fromRequestDto(skillRequestDto);
        skillService.update(updatedSkill, skillId);
        return ResponseEntity.ok(ResponseMessage.ok("Skill updated successfully", null).getBody());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('DELETE_SKILL')")
    public ResponseEntity<ResponseMessage> deleteSkill(@PathVariable Long id) {
        Optional<Skill> existingSkill = skillService.findById(id);
        if (existingSkill.isEmpty()) {
            return ResponseMessage.notFound("Skill not found with ID: " + id);
        }
        skillService.delete(id);
        return ResponseMessage.ok("Skill deleted successfully with ID: " + id, null);
    }
    @GetMapping("/findByName/{name}")
    @PreAuthorize("hasAnyAuthority('VIEW_SKILL')")
    public ResponseEntity<List<UserSkillsResponse>> findSkillsByName(@PathVariable String name) {
        List<Skill> skills = skillService.findSkillsByName(name);
        List<UserSkillsResponse> skillResponseDtos = skills.stream().map(userSkillsMapper::toResponseDto).toList();
        return ResponseEntity.ok(skillResponseDtos);
    }

    @GetMapping("/findByUser")
    @PreAuthorize("hasAnyAuthority('VIEW_SKILL')")
    public ResponseEntity<List<UserSkillsResponse>> findSkillsByUser() {
        List<Skill> skills = skillService.findSkillsByUser();
        List<UserSkillsResponse> skillResponseDtos = skills.stream().map(userSkillsMapper::toResponseDto).toList();
        return ResponseEntity.ok(skillResponseDtos);
    }

}
