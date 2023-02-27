package shop.mtcoding.job.service;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.job.dto.enterprise.EnterpriseReqDto.JoinEnterpriseReqDto;
import shop.mtcoding.job.dto.enterprise.EnterpriseReqDto.LoginEnterpriseReqDto;
import shop.mtcoding.job.handler.exception.CustomException;
import shop.mtcoding.job.model.enterprise.Enterprise;
import shop.mtcoding.job.model.enterprise.EnterpriseRepository;
import shop.mtcoding.job.util.saltEncoder;
import shop.mtcoding.job.util.sha256Encoder;

@Service
public class EnterpriseService {

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Transactional(readOnly = true)
    public Enterprise 기업로그인하기(LoginEnterpriseReqDto loginEnterpriseReqDto) {
        try {
            String salt = enterpriseRepository.findSaltByEnterprisename(loginEnterpriseReqDto.getEnterpriseName());
            if (salt == null) {
                throw new CustomException("아이디가 존재하지 않습니다");
            }
            String sha256Hash = sha256Encoder.sha256(loginEnterpriseReqDto.getPassword());
            Enterprise principalEnt = enterpriseRepository.findByEnterprisenameAndPassword(
                    loginEnterpriseReqDto.getEnterpriseName(),
                    sha256Hash + "_" + salt);
            return principalEnt;
        } catch (NoSuchAlgorithmException e) {
            System.err.println("알고리즘을 찾을 수 없습니다: " + e.getMessage());
        }
        return null;
    }

    @Transactional
    public void 기업가입하기(JoinEnterpriseReqDto joinEnterpriseReqDto) {
        // 1. 유저 유효성 검사
        Enterprise sameEnterprise = enterpriseRepository.findByName(joinEnterpriseReqDto.getEnterpriseName());

        if (sameEnterprise != null) {
            throw new CustomException("동일한 아이디가 존재합니다");
        }
        // 2. 암호화 후 db에 insert하기
        try {
            String sha256Hash = sha256Encoder.sha256(joinEnterpriseReqDto.getPassword());
            String salt = saltEncoder.salt();
            int result = enterpriseRepository.insert(joinEnterpriseReqDto.getEnterpriseName(), sha256Hash + "_" + salt,
                    salt,
                    joinEnterpriseReqDto.getAddress(), joinEnterpriseReqDto.getContact(),
                    joinEnterpriseReqDto.getImage(), joinEnterpriseReqDto.getEmail(), joinEnterpriseReqDto.getSize(),
                    joinEnterpriseReqDto.getSector()

            );
            if (result != 1) {
                throw new CustomException("회원가입이 실패하였습니다");
            }
        } catch (NoSuchAlgorithmException e) {
            System.err.println("알고리즘을 찾을 수 없습니다: " + e.getMessage());
        }
    }

}
