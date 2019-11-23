<?php

defined('BASEPATH') OR exit('No direct script access allowed');

require APPPATH . '/libraries/REST_Controller.php';

class LOGIN_REST extends REST_Controller{

    public function __construct() {
        parent::__construct();
        $this->load->model('LOGIN_model');
        $this->load->model('REGISTRATION_model');
        $this->load->model('ManageOrderOtpModel');
        //$this->load->model('SUBCATEGORY_model');
    }    
    public function data_get($id_param = NULL){
        $arr = array('conditions'=>$this->_get_args);
        $login = $this->LOGIN_model->getRows($arr);
       

        if($login) {           
            $results['login'] = $login;
            $this->response($results, 200);
        } else {
            $this->response(NULL, 404);
        }
    }



public function dataUser_get($id_param = NULL){
        $arr = array('conditions'=>$this->_get_args);
        $login = $this->LOGIN_model->getRows2($arr);
       

        if($login) {           
            $results['login'] = $login;
            $this->response($results, 200);
        } else {
            $this->response(NULL, 404);
        }
    }


	 function data_post() {
       
       $registration_data = array();
       
        $registration_data['isEncypted'] = $this->input->post('isEncypted');
        $sender = $this->input->post('sender');
        $rec = $this->input->post('receiver');
        $response =array();
        $result = $this->LOGIN_model->update($sender,$rec,$registration_data);
        if ($result) {
            # code...
            $response['status'] = "200";
            $response['msg'] ="update success";  
            $response1[] = $response;
            $res['result'] = $response1;
            $this->response($res, 200);
        }else{
            $response['status'] = "400";
            $response['msg'] ="update Fail";  
            $response1[] = $response;
            $res['result'] = $response1;
            $this->response($res, 400);
        }
       
    
            
        }

        function postSavedToken_post() {
       
       $registration_data = array();
       
        $registration_data['token'] = $this->input->post('token');
        $id = $this->input->post('id');
       
        $response =array();
        $result = $this->LOGIN_model->updateToken($id,$registration_data);
        if ($result) {
            # code...
            $response['status'] = "200";
            $response['msg'] ="update success";  
            $response1[] = $response;
            $res['result'] = $response1;
            $this->response($res, 200);
        }else{
            $response['status'] = "400";
            $response['msg'] ="update Fail";  
            $response1[] = $response;
            $res['result'] = $response1;
            $this->response($res, 400);
        }
       
    
            
        }
       
        function data2_post() {
       
       $registration_data = array();
       $otp = rand(100000, 999999);
       
        $registration_data['first_name'] = $this->input->post('first_name');
        $registration_data['last_name'] = $this->input->post('last_name');
        $registration_data['password'] = $this->input->post('password');
        $registration_data['email'] = $this->input->post('email');
        $registration_data['mobile'] = $this->input->post('mobile');
        $registration_data['gender'] = $this->input->post('gender');
        $registration_data['token'] = $this->input->post('token');
        $registration_data['profile_pic'] = $this->input->post('profile_pic');
        $registration_data['otp'] =$otp; 
        $registration_data['active'] = $this->input->post('active');
       
       
      if(array_key_exists("id",$this->input->input_stream())) {
            
            $id = $this->input->post('id');                         
            $result = $this->REGISTRATION_model->update($id,$registration_data);
            
            if($result) {
                

                $responseArray['sucessArrayName'] = array('status' =>'update success');
                       
                $this->response($responseArray, 200);
          
        } else {
            $responseArray['sucessArrayName'] = array('status' =>'failed');
                       
            $this->response($responseArray, 404);
        
        }
        } else{
            $result = $this->REGISTRATION_model->insert($registration_data);
        if($result) {     
             $this->ManageOrderOtpModel->sendotpGET( $registration_data['mobile'],$otp);                  
              $responseArray['sucessArrayName'] = array('status'=> 'inset success',$otp);         
            $this->response($responseArray, 200);
        } else {
            $responseArray['sucessArrayName'] = array('status' =>'failed');
            $this->response($responseArray, 404);
        }
            
        }    

}
function activeuser_post() {
       
       $registration_data = array();

        $registration_data['active'] = $this->input->post('active');
       
       
       
      if(array_key_exists("mobile",$this->input->input_stream())) {
            
            $mobile = $this->input->post('mobile');                         
            $result = $this->REGISTRATION_model->updatePassword($mobile,$registration_data);
            
            if($result) {
                
                $responseArray['sucessArrayName'] = array('status' =>'update success');
                       
                $this->response($responseArray, 200);
          
        } else {
            $responseArray['sucessArrayName'] = array('status' =>'failed');
                       
            $this->response($responseArray, 404);
        }
        
        } else{
            $result = $this->REGISTRATION_model->insert($registration_data);
        if($result === TRUE) {           
              $responseArray['sucessArrayName'] = array('status'=> 'inset success');         
            $this->response($responseArray, 200);
        } else {
            $responseArray['sucessArrayName'] = array('status' =>'failed');
            $this->response($responseArray, 404);
        }
            
        }      

}
       

}