<?php

defined('BASEPATH') OR exit('No direct script access allowed');

require APPPATH . '/libraries/REST_Controller.php';

class MSG_REST extends REST_Controller{

    public function __construct() {
        parent::__construct();
        $this->load->model('MSG_model');
        $this->load->model('ManageOrderOtpModel');
        
    }    
    public function data_get($id_param = NULL){
        $arr = array('conditions'=>$this->_get_args);
        $msg = $this->MSG_model->getRows($arr);
       // $msg1 = $this->MSG_model->getRowsRec($arr);

       

        if($msg) {           
            $results['msg'] = $msg;
                $this->response($results, 200);
            
                       
            
        } else {
            $this->response(NULL, 404);
        }
    }

    public function receiver_get($id_param = NULL){
        $arr = array('conditions'=>$this->_get_args);
        $msg = $this->MSG_model->getRowsRec($arr);
       // $msg1 = $this->MSG_model->getRowsRec($arr);
       

        if($msg) {           
            $results['msg'] = $msg;
            
                $this->response($results, 200);
            
                       
            
        } else {
            $this->response(NULL, 404);
        }
    }
	
	 public function all_get($id_param = NULL){
        //$arr = array('conditions'=>$this->_get_args);
        $arr = array('conditions'=>$this->_get_args);
        //$msg = $this->MSG_model->getR($arr);
       

      // print_r($category_data); die();
       
        $msg = $this->MSG_model->getAll($arr);
        if($msg) {           
            $results['msg'] = $msg;
                       
            $this->response($results, 200);
        } else {
            $this->response(NULL, 404);
        }
    }

    function data_post() {
       
       $registration_data = array();
       
        $registration_data['sender'] = $this->input->post('sender');
        $registration_data['receiver'] = $this->input->post('receiver');
        $registration_data['messages'] = $this->input->post('messages');
        $registration_data['private_key'] = $this->input->post('private_key');
        $registration_data['date_time'] = $this->input->post('date_time');
        $registration_data['active'] = $this->input->post('active');
        $registration_data['isEncypted'] = $this->input->post('isEncypted');

        //$this->ManageOrderOtpModel->sendsmsGET($registration_data['receiver'],$registration_data['private_key'],);
        
       
      if(array_key_exists("id",$this->input->input_stream())) {
            
            $id = $this->input->post('id');                         
            $result = $this->Msg_model->update($id,$registration_data);
            
            if($result) {
                
                $responseArray['sucessArrayName'] = array('status' =>'update success');
                       
                $this->response($responseArray, 200);
          
        } else {
            $responseArray['sucessArrayName'] = array('status' =>'failed');
                       
            $this->response($responseArray, 404);
        }
        
        } else{
            $result = $this->MSG_model->insert($registration_data);
            
            if($result) {           
               
            //$this->MSG_model->getAll($arr);
            $this->ManageOrderOtpModel->sendsmsGET($registration_data['receiver'],$registration_data['private_key'],$result);        
           // $this->response($responseArray, 200);
        } else {

            //$this->response($responseArray, 404);
        }
            
        }
       
        
       

}


	 
}