package com.csmtech.copilot;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
public class DemoController {

    private final UserDetailsMainRepository userDetailsMainRepository;

    private final JwtUtil jwtUtil;

    private final CopilotDemoApplication copilotDemoApplication;
	
	@Autowired
	private ApplicantDetailsRepository repository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtTokenProvider;
	
	@Autowired
	private PocFileRepository pocFileRepository;

    DemoController(CopilotDemoApplication copilotDemoApplication, JwtUtil jwtUtil, UserDetailsMainRepository userDetailsMainRepository) {
        this.copilotDemoApplication = copilotDemoApplication;
        this.jwtUtil = jwtUtil;
        this.userDetailsMainRepository = userDetailsMainRepository;
    }
	
	 public static String generateRandom7DigitNumber() {
		 Random random = new Random();
	        int number = random.nextInt(10000000); // Generates a number between 0 and 9999999
	        return String.format("%07d", number); 
	    }
	
	   @PostMapping("/public/save")
	    public Map<String,Object> save(@RequestBody ApplicantDetails data) {
		   Map<String,Object> map= new HashMap();
		   try {
			   Integer count = 0;
			   count = repository.getCountBymobileNo(data.getMobile());
			   if(count > 0) {
				   map.put("status", 401);
				   map.put("message", "Mobile number already exists");
				   return map;
			   }
			   count = 0;
			   String applicationNo = null;
			   do {
				   applicationNo = data.getApplicantName().substring(0, 3).toUpperCase()+generateRandom7DigitNumber();
				   count = repository.getCountByApplicationNo(applicationNo);
			   }while(count > 0);				   
			   data.setApplicationNo(applicationNo);
			   data.setFinalsubmitstatus(1);
//			   data = repository.save(data);
			   map.put("status", 200);
			   map.put("message", "Success");
			   map.put("data", data.getId());
		   }catch (Exception e) {
			   map.put("status", 400);
			   map.put("message", e.getMessage());
		   }
		   return map;
	    }
	   
	   @GetMapping("/public/findLatestRecord")
	   public Map<String,Object> getLatestRecord(@RequestParam(value = "userid" , required = false) Long userid) {
		   Map<String,Object> map= new HashMap();
		   try {
			   ApplicantDetails data = null;
			   if(userid == null) {
				   List<ApplicantDetails> list = repository.getalldata();
				   data = list.stream().findFirst().get();
			   }else {
				   data = repository.findById(userid).orElse(null);
			   }
			   
			   if(data == null) {
				   map.put("status", 400);
				   map.put("message", "No data found");
				   return map;
			   }
			   
			   List<PocFile> filelist = pocFileRepository.getfiledata(data.getId());
			   PocFile filedata = null;
			   if(filelist != null &&  !filelist.isEmpty()) {
				   filedata = filelist.stream().findFirst().get();
			   }
			   map.put("status", 200);
			   map.put("message", "Success");
			   map.put("data", data);
			   map.put("file", filedata);
		   }catch (Exception e) {
			   map.put("status", 400);
			   map.put("message", e.getMessage());
		   }
		   return map;
	    }
	   
	   @PostMapping("/public/savefile")
	    public Map<String, Object> saveFiles(
	            @RequestParam("photo") MultipartFile photo,
	            @RequestParam("signatureeng") MultipartFile signatureEng,
	            @RequestParam("signaturehindi") MultipartFile signatureHindi,
	            @RequestParam("resident") MultipartFile resident,
	            @RequestParam("physical") MultipartFile physical,
	            @RequestParam("matric") MultipartFile matric,
	            @RequestParam("gradutaion") MultipartFile graduation,
	            @RequestParam("gradutaionmark") MultipartFile graduationMark,
	            @RequestParam("applicantid") Long applicantid) {
	        
	        Map<String, Object> response = new HashMap<>();
	        try {	            
	        	storeFile(photo, "photo");
	            storeFile(signatureEng, "signatureeng");
	            storeFile(signatureHindi, "signaturehindi");
	            storeFile(resident, "resident");
	            storeFile(physical, "physical");
	            storeFile(matric, "matric");
	            storeFile(graduation, "graduation");
	            storeFile(graduationMark, "graduationmark");
	            
	            PocFile pocFile = new PocFile();
	            pocFile.setPhoto(storeFile(photo, "photo"));
	            pocFile.setSignatureEng(storeFile(signatureEng, "signatureeng"));
	            pocFile.setSignatureHindi(storeFile(signatureHindi, "signaturehindi"));
	            pocFile.setResident(storeFile(resident, "resident"));
	            pocFile.setPhysical(storeFile(physical, "physical"));
	            pocFile.setMatric(storeFile(matric, "matric"));
	            pocFile.setGraduation(storeFile(graduation, "graduation"));
	            pocFile.setGraduationMark(storeFile(graduationMark, "graduationmark"));
	            
//	            List<ApplicantDetails> list = repository.getalldata();
//				ApplicantDetails data = list.stream().findFirst().get();
	            
	            ApplicantDetails data = repository.findById(applicantid).orElse(null);
				   
	            pocFile.setApplicant(data);
	            pocFile.setCreatedOn(new Date());
	            
	            pocFileRepository.save(pocFile);	            
	            response.put("status", 200);
	            response.put("message", "Success");
	        } catch (Exception e) {
	            response.put("status", 400);
	            response.put("message", e.getMessage());
	        }
	        return response;
	    }
	   
	   @GetMapping("/public/finalsubmit")
	   @Transactional
	   public Map<String, Object> finalsubmit(
	            @RequestParam("applicantid") Long applicantid) {
	        
	        Map<String, Object> response = new HashMap<>();
	        try {		            
	            ApplicantDetails data = repository.findById(applicantid).orElse(null);	            
	            
	            UserDetailsMain main = new UserDetailsMain();
	            main.setUsername(data.getApplicationNo());
	            main.setAuthValue(passwordEncoder.encode("AIPOC@123"));
	            main.setGroupId(3);
	            main.setCreatedTime(new Date());
	            main.setPhone(data.getMobile());
	            main.setEmail(data.getEmail());
	            main.setPasswordUpdateTime(null);
	            main.setPasswordUpdateFlag(1);
	            main.setStatusFlag(0);
	            main = userDetailsMainRepository.save(main);
	            
	            data.setFinalsubmitstatus(0);
	            data.setUserid(main.getUserId());
	            repository.save(data);
	            
	            response.put("status", 200);
	            response.put("message", "Success");
	        } catch (Exception e) {
	            response.put("status", 400);
	            response.put("message", e.getMessage());
	        }
	        return response;
	    }
	   
	   public String storeFile(MultipartFile file, String filename) throws Exception {
	        try {
	            // Generate the timestamp
	            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
	            // Create the new filename
	            String originalFilename = file.getOriginalFilename();
	            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
	            String newFilename = "poc_" + filename + "_" + timestamp+fileExtension;
	            // Create the directory if it doesn't exist
	            File directory = new File("c:/pocfile/"+filename);
	            if (!directory.exists()) {
	                directory.mkdirs();
	            }
	            // Create the file
	            File newFile = new File(directory, newFilename);
	            try (FileOutputStream fos = new FileOutputStream(newFile)) {
	                fos.write(file.getBytes());
	            }
	            return newFilename;
	        } catch (Exception e) {
	        	throw new Exception(e);
	        }
	    }
	   
	   @GetMapping("/public/download")
	    public ResponseEntity<Resource> downloadFile(@RequestParam String fileName) {
	        try {
	        	String folder = fileName.split("_")[1];
	            // Construct the full file path
	            Path filePath = Paths.get("c:/pocfile/"+folder+"/").resolve(fileName).normalize();
	            Resource resource = new UrlResource(filePath.toUri());

	            if (!resource.exists()) {
	                return ResponseEntity.notFound().build();
	            }

	            // Set the content disposition header for file download
	            return ResponseEntity.ok()
	                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
	                    .body(resource);

	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.internalServerError().build();
	        }
	    }
	   
	   @GetMapping("/public/validateCredentials")
	    public Map<String, Object> validateCredentials(@RequestParam String username, @RequestParam String password) {
		   Map<String, Object> response = new HashMap<>();
	        try {
//	            Integer count = repository.getCountByApplicationNo(username);
//	            if(count > 0) {
//	            	if(password.equals("AIPOC@123")) {
//	            		ApplicantDetails data = repository.getByApplicationNo(username);
//	            		response.put("status", 200);
//	            		response.put("userid", data.getId());
//	    	            response.put("message", "Success");
//	            	} else {
//	            		response.put("status", 400);
//	    	            response.put("message", "Invalid credentials");
//	            	}
//	            } else {
//	            	response.put("status", 400);
//	            	response.put("message", "Invalid credentials");
//	            }
	        	 Authentication authentication = authenticationManager.authenticate(
    	            new UsernamePasswordAuthenticationToken(username, password)
    	        );

    	        // Generate JWT token
    	        String token = jwtTokenProvider.generateToken(username);
    	        
    	        UserDetailsMain user = userDetailsMainRepository.findByUsername(username);
    	        if(user.getGroupId() == 3) {
    	        	ApplicantDetails data = repository.getByApplicationNo(username);
    	        	response.put("userid", data.getId());
    	        	response.put("password", user.getPasswordUpdateFlag() == null ? 1 : user.getPasswordUpdateFlag());
    	        }else {
		        	response.put("password", 0);
		        }
    	        response.put("status", 200);
            	response.put("message", "Success");
            	response.put("token", token);
            	
	        } catch (Exception e) {
	            e.printStackTrace();
	            response.put("status", 400);
	            response.put("message", "Invalid credentials");
	        }
	        return response;
	    }

	   @GetMapping("/public/checkmobileno")
	    public Map<String, Object> checkmobileno(@RequestParam String mobile) {
		   Map<String, Object> response = new HashMap<>();
	        try {
	        	Integer count = repository.getCountBymobileNo(mobile);
	        	if(count > 0) {
	        		ApplicantDetails appli = repository.getBymobileNo(mobile);
	        		response.put("status", 200);
	        		response.put("message", "Success");
	        		response.put("applicantid", appli.getId());
	        		response.put("userid", appli.getUserid());
	        	}else {
	        		response.put("status", 401);
	        		response.put("message", "Mobile number not found");
	        	}
	        } catch (Exception e) {
	            e.printStackTrace();
	            response.put("status", 400);
	            response.put("message", e.getMessage());
	        }
	        return response;
	    }	
	   
	   @GetMapping("/public/changePassword")
	    public Map<String, Object> changePassword(@RequestParam Long userid, @RequestParam String newPassword) {
		   Map<String, Object> response = new HashMap<>();
	        try {
	        	UserDetailsMain user = userDetailsMainRepository.findById(userid).get();
	        	if(user != null) {
	        		user.setAuthValue(passwordEncoder.encode(newPassword));
	        		user.setPasswordUpdateFlag(0);
	        		user.setPasswordUpdateTime(new Date());
	        		userDetailsMainRepository.save(user);
	        		response.put("status", 200);
	        		response.put("message", "Success");
	        	}else {
	        		response.put("status", 401);
	        		response.put("message", "User not found");
	        	}
	        } catch (Exception e) {
	            e.printStackTrace();
	            response.put("status", 400);
	            response.put("message", e.getMessage());
	        }
	        return response;
	   }
	   
	   @GetMapping("/public/getimagefile")
		public ResponseEntity<?> getProfilePhoto(@RequestParam Long status, @RequestParam String filepath) {
			try {
				String folder = filepath.split("_")[1];
				String profilePath = "c:/pocfile/"+folder+"/"+filepath;
				if (profilePath != null) {
					Path path = Paths.get(profilePath);
					ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
					MediaType type = MediaType.IMAGE_JPEG;
					String filetype = filepath.substring(filepath.lastIndexOf('.'));
					if (filetype.equalsIgnoreCase(".png")) {
						type = MediaType.IMAGE_PNG;
					} else if (filetype.equalsIgnoreCase(".jpg")) {
						type = MediaType.IMAGE_JPEG;
					} else if (filetype.equalsIgnoreCase(".jpeg")) {
						type = MediaType.IMAGE_JPEG;
					}
					return ResponseEntity.ok().contentLength(path.toFile().length()).contentType(type)
							.body(resource);
				}
			} catch (IOException e) {
				 e.printStackTrace();
			} catch (Exception e) {
				 e.printStackTrace();
			}
			return null;
		}

}
