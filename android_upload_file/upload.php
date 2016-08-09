<?php
/**
 * Created by PhpStorm.
 * User: zhangyipeng
 * Date: 16/2/18
 * Time: 下午2:19
 */
$base_path = "./upload_file/"; //接收文件目录
$target_path = $base_path . basename ( $_FILES ['uploadfile'] ['name'] );
if (move_uploaded_file ( $_FILES ['uploadfile'] ['tmp_name'], $target_path )) {
    $array = array ("code" => "1", "message" => $_FILES ['uploadfile'] ['name']
    ,"imgUrl" => $target_path);
    echo json_encode ( $array );
} else {
    $array = array ("code" => "0", "message" => "There was an error uploading the file, please try again!" . $_FILES ['uploadfile'] ['error'] );
    echo json_encode ( $array );
}