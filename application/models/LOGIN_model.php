<?php

defined('BASEPATH') OR exit('No direct script access allowed');

class LOGIN_model extends CI_Model{
    function __construct() {
        $this->userTbl = 'users';
        $this->userTb2 = 'messages';
		$this->load->database();
    }

    function getRows($params = array()){
       
        $this->db->select('*');
        $this->db->from($this->userTbl);
        //fetch data by conditions
        if(array_key_exists("conditions",$params)){
            foreach ($params['conditions'] as $key => $value) {
                $this->db->where($key,$value);
                
            }
        }
        
        if(array_key_exists("id",$params)){
            $this->db->where('id',$params['id']);
            $query = $this->db->get();
            $result = $query->row_array();
		}            
            $query = $this->db->get();
             
				$result = ($query->num_rows() > 0)?$query->result_array():FALSE;
            
        
        return $result;
    }


    function getRows2($params = array()){
       // $sender = $params["conditions"]["sender"];
        $a='7800907494';
        $this->db->select('*');
        $query =$this->db->from($this->userTbl)->get();
        $result = ($query->num_rows() > 0)?$query->result_array():FALSE;
       
        

        $this->db->distinct();
        $this->db->select('*');
        $query1 =$this->db->from($this->userTb2)->get();
        $result1 = ($query1->num_rows() > 0)?$query1->result_array():FALSE;
        print_r($result1);
        
        //$this->db->join($this->userTb2,'users.mobile= messages.receiver');
        //fetch data by conditions
        if(array_key_exists("conditions",$params)){
            foreach ($params['conditions'] as $key => $value) {
                $this->db->where($key,$value);
                
            }
        }
        
        if(array_key_exists("id",$params)){
            $this->db->where('id',$params['id']);
            $query = $this->db->get();
            $result = $query->row_array();
        }            
            $query = $this->db->get();
             
                
            
        
        return $result;
    }

    function update($sender,$res,$data = array()) {
        //printf($user_id);
    
        $this->db->where('sender', $sender);
        $this->db->where('receiver', $res);
        $this->db->or_where("receiver",$sender);
        $this->db->where("sender",$res);

        $result = $this->db->update('messages', $data);

        return $result;
    }  

    function updateToken($id,$data = array()) {
        //printf($user_id);

        $this->db->where("mobile",$id);

        $result = $this->db->update('users', $data);

        return $result;
    }  

    
}