package com.imau.brms.service;

import com.imau.brms.entity.ReaderCard;
import com.imau.brms.entity.ReaderInfo;
import com.imau.brms.mapper.ReaderMapper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;

@Service
public class ImportReaderService {

    @Autowired
    private ReaderMapper readerMapper;

    public int addReader(MultipartFile file) throws Exception{

        int result = 0;
        //存放excel表中所有读者信息
        ArrayList<ReaderCard> readerCards = new ArrayList<>();
        ArrayList<ReaderInfo> readerInfos = new ArrayList<>();
        /**
         *
         * 判断文件版本
         */
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".")+1);

        InputStream ins = file.getInputStream();

        Workbook wb = null;

        if(suffix.equals("xlsx")){

            wb = new XSSFWorkbook(ins);

        }else{
            wb = new HSSFWorkbook(ins);
        }
        /**
         * 获取excel表单
         */
        Sheet sheet = wb.getSheetAt(0);


        /**
         * line = 2 :从表的第三行开始获取记录
         *
         */
        if(null != sheet){

            for(int line = 2; line <= sheet.getLastRowNum();line++){

                ReaderCard readerCard = new ReaderCard();
                ReaderInfo readerInfo = new ReaderInfo();

                Row row = sheet.getRow(line);

                if(null == row){
                    continue;
                }
                /**
                 * 判断单元格类型是否为文本类型
                 * 改------------------------------------
                 */
                if(1 != row.getCell(0).getCellType()){
                    throw new Exception("is not text");
                }

                /**
                 * 获取单元格的内容
                 */
                Long readerId = Long.parseLong(row.getCell(0).getStringCellValue());
                String name = row.getCell(1).getStringCellValue();
                String department = row.getCell(2).getStringCellValue();
                String major = row.getCell(3).getStringCellValue();
                String grade = row.getCell(4).getStringCellValue();
                String className = row.getCell(5).getStringCellValue();

                readerInfo.setReaderId(readerId);
                readerInfo.setName(name);
                readerInfo.setDepartment(department);
                readerInfo.setMajor(major);
                readerInfo.setGrade(grade);
                readerInfo.setClassName(className);

                readerCard.setReaderId(readerId);
                readerCard.setName(name);
                readerCard.setPasswd("bb8ce661128c8341533f10b34576d49ecac94c0b31edc6864f13bb76171c92e9");

                readerCards.add(readerCard);
                readerInfos.add(readerInfo);

            }

            for(ReaderCard readerCard:readerCards){
                /**
                 * 判断数据库表中是否存在用户记录，若存在，则更新，不存在，则保存记录
                 */
                Long readerId = readerCard.getReaderId();

                int count = readerMapper.selectReaderCard(readerId);

                if(0 == count){
                    result = readerMapper.insertCard(readerCard);
                }else{
                    result = readerMapper.updateReaderCard(readerCard);
                }
            }

            for(ReaderInfo readerInfo:readerInfos){
                /**
                 * 判断数据库表中是否存在用户记录，若存在，则更新，不存在，则保存记录
                 */
                Long readerId = readerInfo.getReaderId();

                int count = readerMapper.selectReaderInfo(readerId);

                if(0 == count){
                    result = readerMapper.insertInfo(readerInfo);
                }else{
                    result = readerMapper.updateReaderInfo(readerInfo);
                }
            }

        }

        return result;
    }
}
