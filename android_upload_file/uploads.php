<?php
/**
 * Created by PhpStorm.
 * User: zhangyipeng
 * Date: 16/2/18
 * Time: 下午3:48
 */

$base_path = "./upload_file/"; //接收文件目录

$imgs = array();  //定义一个数组存放上传图片的路径
$isSave = false;
if (!file_exists($base_path)) {
    mkdir($base_path);
}

foreach ($_FILES["uploadfile"]["error"] as $key => $error) {
    if ($error == UPLOAD_ERR_OK) {
        $tmp_name = $_FILES["uploadfile"]["tmp_name"][$key];
        $name = $_FILES["uploadfile"]["name"][$key];
        $uploadfile = $base_path . $name;
        $isSave = move_uploaded_file($tmp_name, $uploadfile);
        if ($isSave){
            $imgs[]=$uploadfile;
        }
    }
}

if ($isSave) {
    $array = array("code" => "1", "message" =>"上传图片成功"
    , "imgUrls" => $imgs);
    echo json_encode($array);
} else {
    $array = array("code" => "0", "message" => "上传图片失败," . $_FILES ['uploadfile'] ['error']
    , "imgUrls" => $imgs);
    echo json_encode($array);
}