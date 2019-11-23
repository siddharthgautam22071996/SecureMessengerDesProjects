<?php

class Registration extends CI_Controller
{

    function __construct() {
        parent::__construct();        
        $this->load->library('Rest', array(
            'server' => 'http://localhost/SecureMessenger',
            'api_key'  => 'REST API',
            'api_name' => 'X-API-KEY',
            'http_user' => 'admin',
            'http_pass' => '1234',
            'http_auth' => 'basic',
        ));       
    }
	
  
   function post($id=0) {
        $this->rest->format('application/json');
        $params = $this->input->post(NULL,TRUE);
        $user = $this->rest->post('index.php/REGISTRATION_REST/data/',$params,'');
        $this->rest->debug();
    }

function password($id=0) {
    
        $this->rest->format('application/json');
        $params = $this->input->post(NULL,TRUE);
        $user = $this->rest->post('index.php/REGISTRATION_REST/password/',$params,'');
        
        $this->rest->debug();
    }
    
  function activeuser($id=0) {
        $this->rest->format('application/json');
        $params = $this->input->post(NULL,TRUE);
        $user = $this->rest->post('index.php/REGISTRATION_REST/activeuser/',$params,'');
        
        $this->rest->debug();
    }
  
	
}



























