<?php

class Msg extends CI_Controller
{

    function __construct() {
        parent::__construct();        
        $this->load->library('Rest', array(
            'server' => 'http://localhost/secureMessenger',
            'api_key'  => 'REST API',
            'api_name' => 'X-API-KEY',
            'http_user' => 'admin',
            'http_pass' => '1234',
            'http_auth' => 'basic',
        ));

        $this->load->database();       
    }
	
  
    function get($id1=0)
	{  //echo "test"; die();    
      $id = $this->uri->uri_to_assoc(3);
      $params = $this->uri->assoc_to_uri($id);
      $params = str_replace("%20","+",$params);
      $this->rest->format('application/json');
      $user = $this->rest->get('index.php/MSG_REST/data/'.$params,'','');
      $this->rest->debug();
    }

    function test_msg($number)
    {
      $this->db->distinct();
      $this->db->select('receiver as number');
      $this->db->where('sender',$number);
      $a = $this->db->get('messages');
      
      $numbers = array();
      foreach($a->result_array() as $aa)
      {
        $numbers[] = $aa['number'];
      }

      $this->db->distinct();
      $this->db->select('sender as number');
      $this->db->where('receiver',$number);
      $b = $this->db->get('messages');

      foreach($b->result_array() as $bb)
      {
        $numbers[] = $bb['number'];
      }

      $numbers = array_unique($numbers);
      $final = array();
      //echo "<pre>"; print_r($final);

      
      
      foreach($numbers as $number)
      {
        $this->db->where('mobile',$number);
        $user = $this->db->get('users')->row();

        /****GET MESSAGE****/
         $this->db->where('sender',$number);
         $this->db->or_where('receiver',$number);
         $msg = $this->db->get('messages')->row();
         /****GET MESSAGE****/

        $final['number'] = $number;
        $final['last_msg'] =$msg->messages;
        $final['isEncypted'] ="".$msg->isEncypted;
        $final['photo'] = $user->profile_pic;
        $final['name'] = $user->first_name." ".$user->last_name;
        //echo $number."<br>"; 
        $final1[] = $final;
      }
      $result['msg'] = $final1;
      
      print(json_encode($result));
      //echo "<prE>"; print_r($final1);
      
    }

     function getRec($id1=0)
  {      
      $id = $this->uri->uri_to_assoc(3);
      $params = $this->uri->assoc_to_uri($id);
      $params = str_replace("%20","+",$params);
      $this->rest->format('application/json');
      $user = $this->rest->get('index.php/MSG_REST/receiver/'.$params,'','');
      $this->rest->debug();
    }
	
	function getAll($id1=0)
	{      
      $id = $this->uri->uri_to_assoc(3);
      $params = $this->uri->assoc_to_uri($id);
      $params = str_replace("%20","+",$params);
      
      
      $this->rest->format('application/json');
      $user = $this->rest->get('index.php/MSG_REST/all/'.$params,'','');
      $this->rest->debug();
	  
	    
    }

    function post($id=0) {
    
        $this->rest->format('application/json');
        $params = $this->input->post(NULL,TRUE);
        $user = $this->rest->post('index.php/MSG_REST/data/',$params,'');
        
        $this->rest->debug();
    }

	
}



























