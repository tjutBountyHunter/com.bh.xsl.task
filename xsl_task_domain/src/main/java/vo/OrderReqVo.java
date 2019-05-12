package vo;

import java.io.Serializable;

public class OrderReqVo implements Serializable
{
   private int page;
   private int rows;
   public void setPage(int page){this.page=page;}
   public void setRows(int rows){this.rows=rows;}
   public int getPage(){return this.page;}
   public int getRows(){return this.rows;}
}
