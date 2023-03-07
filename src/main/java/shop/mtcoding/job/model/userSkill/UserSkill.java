package shop.mtcoding.job.model.userSkill;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserSkill {
    private int id;
    private int userId;
    private Integer skill;


    public void setSkill(Integer skill) {
        skill = null;
    }
}
