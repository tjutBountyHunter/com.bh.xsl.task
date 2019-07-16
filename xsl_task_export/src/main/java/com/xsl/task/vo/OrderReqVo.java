package com.xsl.task.vo;

public class OrderReqVo extends ReqBaseVo{
   private int page;
   private int rows;

   public int getPage() {
      return page;
   }

   public void setPage(int page) {
      this.page = page;
   }

   public int getRows() {
      return rows;
   }

   public void setRows(int rows) {
      this.rows = rows;
   }
}
