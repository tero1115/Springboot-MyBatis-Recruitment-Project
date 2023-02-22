package shop.mtcoding.job.model.enterprise;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EnterpriseRepository {
        public List<Enterprise> findAll();

        public Enterprise findById(int id);

        public int insert(@Param("enterpriseName") String enterpriseName, @Param("password") String password,
                        @Param("salt") String salt,
                        @Param("address") String address,
                        @Param("contact") String contact,
                        @Param("image") String image,
                        @Param("email") String email, @Param("size") String size,
                        @Param("sector") String sector);

        public int updateById(Enterprise enterprise);

        public int deleteById(int id);

        public Enterprise findByName(String enterpriseName);
}
