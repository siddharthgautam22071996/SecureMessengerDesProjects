<?php

defined('BASEPATH') OR exit('No direct script access allowed');

class MSG_model extends CI_Model{
    function __construct() {
        $this->userTbl = 'messages';
        $this->userTb2 = 'users';
		$this->load->database();
    }

    function getRows($params = array()){
       
       //print_r($params); die();
        
        $this->db->group_by('messages.sender ');
        $this->db->from($this->userTbl);
		//$this->db->groupby
        $this->db->join($this->userTb2, 'messages.sender= users.mobile');
		
		
        //fetch data by conditions
        if(array_key_exists("conditions",$params)){
            foreach ($params['conditions'] as $key => $value) {
                $this->db->where($key,$value);
				
            }
        }
        
        /*if(array_key_exists("id",$params)){
            $this->db->where('id',$params['id']);
            $query = $this->db->get();
            $result = $query->row_array();
		} */           
            $query = $this->db->get();
             
				$result = ($query->num_rows() > 0)?$query->result_array():FALSE;
            
        
        return $result;
    }

    function getRowsRec($params = array()){
       
       //print_r($params); die();
        
        $this->db->group_by('messages.receiver');
        $this->db->from($this->userTbl);
        //$this->db->groupby
        $this->db->join($this->userTb2, 'messages.receiver= users.mobile');
        
        
        //fetch data by conditions
        if(array_key_exists("conditions",$params)){
            foreach ($params['conditions'] as $key => $value) {
                $this->db->where('sender',$value);
                
            }
        }
        
        /*if(array_key_exists("id",$params)){
            $this->db->where('id',$params['id']);
            $query = $this->db->get();
            $result = $query->row_array();
        } */           
            $query = $this->db->get();
             
                $result = ($query->num_rows() > 0)?$query->result_array():FALSE;
            
        
        return $result;
    }
	

	function getAll($params = array()){
       // print_r($params); 
        $sender = $params["conditions"]["sender"]; $receiver=$params["conditions"]["receiver"];
        $this->db->select('*');
        $this->db->from($this->userTbl);
		$this->db->where("active",'1');
		$this->db->where("sender",$sender);
		$this->db->where("receiver",$receiver);
        $this->db->or_where("sender",$receiver);
        $this->db->where("receiver",$sender);
        
        $query = $this->db->get();
             
                $result = ($query->num_rows() > 0)?$query->result_array():FALSE;
            
        
        return $result;
        
    }

    public function insert($data = array()) { 
        //insert user data to users table
      //  print_r($data); die();
    
        $this->db->insert("messages", $data);
        return $insert;
          
    }
    
function update($id,$data = array()) {
        //printf($user_id);
        
        print_r($data);
        $this->db->where('id', $id);
        $result = $this->db->update($this->userTbl, $data);
        return $result;
    }  

    
}