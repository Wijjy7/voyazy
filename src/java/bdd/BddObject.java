package bdd;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Vector;

public class BddObject {
    
    String pkPrefixe;
    int pkLongueur;

    public Vector select(Connection connection , String table) throws Exception
    {
        Statement stat=connection.createStatement();
        Vector listObjet=new Vector();
        Vector<Field> listField=BddObject.getFields(connection, this , table);

        String sql ="SELECT * FROM "+table.toUpperCase();
        ResultSet resultSet=stat.executeQuery(sql);
        while (resultSet.next()) 
        {
            Object objet=this.getResultSetObject(listField,resultSet);
            listObjet.add(objet);
        }
        stat.close(); 

        System.out.println(sql);
        return listObjet;
    }       

    public Object selectById(Connection connection , String table , Object id) throws Exception
    {
        String primaryKey = BddObject.getPrimaryKeyName(connection,table);
        Statement stat=connection.createStatement();
        Vector listObjet=new Vector();
        Vector<Field> listField=BddObject.getFields(connection, this , table);

        String sql = "SELECT * FROM "+table.toUpperCase()+" WHERE "+primaryKey+" = '"+String.valueOf(id)+"'";
        ResultSet resultSet=stat.executeQuery(sql);
        Object objet = null;
        while(resultSet.next()){
            objet=this.getResultSetObject(listField,resultSet);
        }
        
        
        System.out.println(sql);
        stat.close(); 

        return objet;
    }    

    public void insert(Connection connection , String table) throws Exception
    {
        Statement statement=connection.createStatement();
        Vector<Field> listField=BddObject.getFields(connection, this , table);

        String colonne="" , donnees="" , primaryKey=null;

        try {
            primaryKey=BddObject.getPrimaryKeyName(connection, table);
        } catch (Exception e) { }

        for (int j = 0; j < listField.size() ; j++) 
        {
            Field temp=(Field)listField.get(j);

            String d=this.setSQLValues(temp);
            if (primaryKey!=null && primaryKey.equals(temp.getName())){
//                d=this.setPrimaryKeyValue(connection, temp, table);
                  d = "default";
            }
            if (j!=listField.size()-1) 
            {
                colonne=colonne+String.valueOf(temp.getName())+",";
                donnees=donnees+d+",";
            }else{
                colonne=colonne+String.valueOf(temp.getName());
                donnees=donnees+d;
            }
        }
        String sql="INSERT INTO "+table+"("+colonne+")"+ " values ("+donnees+")";
        System.out.println(sql);

        statement.execute(sql);
        statement.close();
    }

    public void update(Connection connection,String table_name) throws Exception
    {
        Statement stat = connection.createStatement();
        String primaryKey = BddObject.getPrimaryKeyName(connection, table_name);
        Field objectField = this.getClass().getDeclaredField(primaryKey);

        Method method = this.getClass().getMethod("get"+BddObject.changeFirstLetter(primaryKey));
        String primarykeyvalue = String.valueOf(method.invoke(this));

        Vector<Field> allObjectFields = BddObject.getFields(connection,this,table_name);
        String modification="";
        for (int i = 0; i < allObjectFields.size(); i++) 
        {
            Field temporaire = (Field)allObjectFields.get(i);            
            String donnee_temp =this.setSQLValues(temporaire);
            if (i != allObjectFields.size()-1) 
            {
                modification = modification+String.valueOf(temporaire.getName())+"="+donnee_temp+",";
            }
            else{
                modification = modification+String.valueOf(temporaire.getName())+"="+donnee_temp;
            }
        }

        String final_request = "UPDATE "+table_name+" SET "+modification+" WHERE "+primaryKey+" = "+"'"+primarykeyvalue+"'";
        System.out.println(final_request);
        stat.executeUpdate(final_request);
        stat.close();  
    }

    public void delete(Connection connection,String table_name) throws Exception
    {
        Statement stat = connection.createStatement();
        String primaryKey = BddObject.getPrimaryKeyName(connection, table_name);

        Method method = this.getClass().getMethod("get"+BddObject.changeFirstLetter(primaryKey));
        String primarykeyvalue = String.valueOf(method.invoke(this));


        String final_request = "DELETE FROM "+table_name.toUpperCase()+" WHERE "+primaryKey+" ='"+primarykeyvalue+"'";
        stat.execute(final_request);
        stat.close();
        System.out.println(final_request);

    }

    public String setSQLValues(Field field)throws Exception{
        String getter="get"+BddObject.changeFirstLetter(field.getName());

        Method method = this.getClass().getMethod(getter);
        String data="'"+method.invoke(this)+"'";

        if (method.invoke(this) instanceof Number) {
            data=String.valueOf(method.invoke(this));
        }
        if(method.invoke(this) instanceof java.sql.Date){
            data="TO_DATE('"+ method.invoke(this) +"' , 'YYYY-MM-DD')";
        }
        if(method.invoke(this) instanceof java.sql.Timestamp){
            data="TO_TIMESTAMP('"+  method.invoke(this) +"' , 'YYYY-MM-DD HH24:MI:SS.FF')";
        }
        return data;
    }
    public String setPrimaryKeyValue(Connection connection , Field field , String table)throws Exception{
        String setter="set"+BddObject.changeFirstLetter(field.getName());
        Method setPkey=this.getClass().getMethod(setter,field.getType());

        String sequence = BddObject.getSequenceName(table);
        String pk = "";
        if(field.getType()==String.class){
            String value = "";
            if (BddObject.hasSeq(connection, table)) {
                value = this.pkString(connection, sequence);
            }else{
                value = this.getNextPKValue(connection,table,field);
            }
            pk = "'"+value+"'";
            setPkey.invoke(this,value);
        }else{
            int nextVal;   
            if (BddObject.hasSeq(connection, table)) {
                nextVal = this.getNextVal(connection, sequence);
            }else{
                nextVal = Integer.parseInt(this.getNextPKValue(connection,table,field));
            }
            pk=String.valueOf( nextVal );
            setPkey.invoke(this,nextVal);
        }
        return pk;
    }

    public String pkString(Connection connection , String sequence)throws Exception{
        String prefixe = this.getPkPrefixe();
        String next_val = String.valueOf(this.getNextVal(connection, sequence));
        for (int i = prefixe.length() ; i < this.getPkLongueur()-next_val.length() ; i++) {
            prefixe+="0";
        }
        return prefixe+next_val;
    }
    public int getNextVal(Connection connection , String sequence)throws Exception{
        String sql = "";
        if (connection.getMetaData().getDriverName().split(" ")[0].equalsIgnoreCase("Oracle")) 
        {
            sql = "select "+sequence+".nextVal from dual";
        }
        if (connection.getMetaData().getDriverName().split(" ")[0].equalsIgnoreCase("PostgreSQL")) 
        {
            sql = "select nextval('"+sequence+"')";
        }
        Statement state=connection.createStatement();
        ResultSet res=state.executeQuery(sql);
        res.next();
        int next = res.getInt(1);
        state.close();
        return next;
    }
    public String getNextPKValue(Connection connection , String table , Field field) throws Exception
    {
        String primaryKeyName = BddObject.getPrimaryKeyName(connection , table);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT MAX("+ primaryKeyName +") as ID  from "+table);
        resultSet.next();
        String maxValue = resultSet.getString("ID");
        if(field.getType()==String.class)
        {    
            String prefixe = this.getPkPrefixe();
            int number = 0;
            if(maxValue!=null){
                number = Integer.parseInt(maxValue.substring(prefixe.length(),maxValue.length()));
            }
            String next_val = String.valueOf(number+1);            
            for (int i = prefixe.length() ; i < this.getPkLongueur()-next_val.length() ; i++) {
                prefixe+="0";
            }
            return prefixe+next_val;
            
        }else{
            if(maxValue==null){
                return String.valueOf(1);
            }
            return String.valueOf(Integer.parseInt(maxValue)+1);
        }
    }
    public Object getResultSetObject(Vector listField , ResultSet resultSet) throws Exception
    {
        Object objet=this.getClass().getConstructor().newInstance();
        for (int i = 0; i < listField.size(); i++) 
        {
            Field field=(Field)listField.get(i);

            String fieldType = BddObject.changeFirstLetter(field.getType().getSimpleName());
            String getterBase="get"+fieldType;

            String setterObject="set"+BddObject.changeFirstLetter(field.getName());

            Method getResultSet=ResultSet.class.getMethod(getterBase,String.class);
            Method setObjet=objet.getClass().getMethod(setterObject,field.getType());

            setObjet.invoke(objet,getResultSet.invoke(resultSet,field.getName()));
        }
        return objet;
    }

    public static String changeFirstLetter(String mot)
    {
        char[] motChar=mot.toCharArray();
        char first=Character.toUpperCase(motChar[0]);
        motChar[0]=first;
        return String.valueOf(motChar);
    }
    
    public static Vector getFields(Connection connection , Object object, String table_name)throws Exception
    {
        Statement statement=connection.createStatement();
        Vector<String> listColumnBase=BddObject.getColumnName(connection, table_name);

        Field[] attributObject=object.getClass().getDeclaredFields();
        Vector<Field> listAttribut=new Vector<Field>();
        for (int i = 0; i < attributObject.length; i++) 
        {
            if (listColumnBase.contains(attributObject[i].getName().toLowerCase())) 
            {
                listAttribut.add(attributObject[i]);
            }
        }
        statement.close();
        return listAttribut;
    }

    public static boolean hasSeq(Connection connection,String table_name) throws Exception
    {
        String sequence=BddObject.getSequenceName(table_name);

        String sql = "";
        if (connection.getMetaData().getDriverName().split(" ")[0].equalsIgnoreCase("Oracle")) 
        {
            sql = "Select * from user_sequences where sequence_name='"+sequence.toUpperCase()+"'";
        }
        if (connection.getMetaData().getDriverName().split(" ")[0].equalsIgnoreCase("PostgreSQL")) 
        {
            sql = "select * from information_schema.sequences where sequence_name='"+sequence.toLowerCase()+"'";
        }
        boolean exists=false;
        Statement state=connection.createStatement();
        try {
            ResultSet res=state.executeQuery(sql);
            int count=0;
            if (res.next()) {
                count=1;
            }
            if (count==1) {
                exists=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            state.close();
        }
        return exists;
    }
    public static Vector getColumnName(Connection connection , String table) throws Exception
    {
        String sql = "";
        if(connection.getMetaData().getDriverName().split(" ")[0].equalsIgnoreCase("Oracle")){
            sql = "Select column_name from USER_TAB_COLUMNS where table_name='"+table.toUpperCase()+"'";
        }
        if(connection.getMetaData().getDriverName().split(" ")[0].equalsIgnoreCase("PostgreSQL")){
            sql = "select COLUMN_NAME from information_schema.columns WHERE table_name='"+table.toLowerCase()+"'";
        }

        Statement stat=connection.createStatement();
        ResultSet resultSet=stat.executeQuery(sql);
       
        Vector<String> colonne=new Vector<String>();
        while(resultSet.next())
        {
            colonne.add(resultSet.getString("COLUMN_NAME").toLowerCase());
        }
        stat.close();
        return colonne;
    }

    public static String getPrimaryKeyName(Connection connection , String table_name)throws Exception
    {
        String sql = "";     
        if(connection.getMetaData().getDriverName().split(" ")[0].equalsIgnoreCase("Oracle"))
        {
            sql = "SELECT cols.column_name FROM all_constraints cons, all_cons_columns cols WHERE cols.table_name = '"+table_name.toUpperCase()+"' AND cons.constraint_type = 'P' AND cons.constraint_name = cols.constraint_name AND cons.owner = cols.owner ORDER BY cols.table_name, cols.position";
        }
        if (connection.getMetaData().getDriverName().split(" ")[0].equalsIgnoreCase("PostgreSQL")) 
        {
            sql ="SELECT a.attname column_name FROM pg_index i JOIN pg_attribute a ON a.attrelid =i.indrelid AND a.attnum= ANY (i.indkey) WHERE i.indrelid='"+table_name+"'::regclass AND i.indisprimary";
        }
        Statement stat=connection.createStatement();
        ResultSet resultSet = stat.executeQuery(sql);
        resultSet.next();
        String primaryKey = "";
        try{
            primaryKey = resultSet.getString("column_name").toLowerCase();
        }catch(Exception e){
            throw new Exception("La table '"+table_name+"' n'a pas de primary key");
        }finally{
            stat.close();
        }
        return primaryKey;
    }

    public static String getSequenceName(String table_name){
        return table_name+"_seq";
    }

    public String getPkPrefixe() {
        if(this.pkPrefixe==null) {
            this.setPkPrefixe(this.getClass().getSimpleName().substring(0, 3).toUpperCase());
        }
        return pkPrefixe;
    }

    public void setPkPrefixe(String pkPrefixe) {
        this.pkPrefixe = pkPrefixe;
    }

    public int getPkLongueur() {
        if(this.pkLongueur==0){
            this.setPkLongueur(7);
        }
        return pkLongueur;
    }

    public void setPkLongueur(int pkLongueur) {
        this.pkLongueur = pkLongueur;
    }
}