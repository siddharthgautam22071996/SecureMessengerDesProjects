<?php

defined('BASEPATH') OR exit('No direct script access allowed');

class REGISTRATION_model extends CI_Model{

    function __construct() {
        $this->userTbl = 'users';
		$this->load->database();
    }

public function insert($data = array()) { 
        //insert user data to users table
      //  print_r($data); die();
    
       $result = $this->db->insert('users', $data);

        return $result;
          
    }

function update($id,$data = array()) {
        //printf($user_id);
        //print_r($data);
        $this->db->where('id', $id);
        $result = $this->db->update($this->userTbl, $data);
        return $result;
    }  

function updatePassword($mobile,$data = array()) {
        //printf($user_id);
    
        $this->db->where('mobile', $mobile);
        $result = $this->db->update($this->userTbl, $data);
        return $result;
    }  

    
}
    