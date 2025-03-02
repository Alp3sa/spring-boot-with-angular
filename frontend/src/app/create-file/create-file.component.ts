import { FileService } from "./../services/file.service";
import { DatabaseService } from "./../services/database.service";
import { DatabaseTableService } from "./../services/database-table.service";
import { DatabaseFieldService } from "./../services/database-field.service";
import { Component, OnInit } from '@angular/core';
import { Database } from '../models/database';
import { DatabaseTable } from '../models/databaseTable';
import { DatabaseField } from '../models/databaseField';
import { UserService } from "./../services/user.service";
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: false,
  selector: 'app-create-file',
  templateUrl: './create-file.component.html',
  styleUrls: ['./create-file.component.css']
})
export class CreateFileComponent implements OnInit {
  combosLoaded: Promise<boolean>;
  isDataLoaded: boolean = false;
  filename: string;
  conditions: string;
  databases: Database[];
  selectedDatabase: Database;
  databaseTables: DatabaseTable[];
  selectableTables: DatabaseTable[];
  selectedTable: DatabaseTable;
  databaseFields: DatabaseField[] = [];
  selectableFields: DatabaseField[];
  selectedField: DatabaseField;
  selectedFieldsDisplayed: DatabaseField[] = [];
  fieldsToExport:DatabaseField[] = [];
  message: string = "";
  processStatus: number = -1;
  isListOfFilesEmpty: boolean = true;

  constructor(private fileService: FileService, private databaseService: DatabaseService, private databaseTableService: DatabaseTableService, private databaseFieldService: DatabaseFieldService, private userService: UserService, private router: Router) { }

  ngOnInit() {
    if(!this.userService.authenticated){
      this.router.navigateByUrl('/login');
    }
    this.loadData();
    this.userService.cookieService.set('lastPage','/createFile');
  }

  loadData() {
    this.databaseService.getList().subscribe(
      database => {this.databases = database;
        this.listFilesIsEmpty();
        if(database!=null && database.length>0){
          this.selectedDatabase=database[0];
          this.loadTables();
        }
      }
    );
  }

  loadTables(){
    this.databaseTableService.getList().subscribe(
      databaseTables => {
      this.databaseTables = databaseTables;
      if(databaseTables!=null && databaseTables.length>0){
        this.selectedTable=databaseTables[0];
        this.loadFields();
        this.setSelectableTables(this.databases[0].id);
      }
    });
  }

  loadFields(){
    this.databaseFieldService.getList().subscribe(
      databaseFields => {
      this.databaseFields = databaseFields;
      if(databaseFields!=null && databaseFields.length>0){
        this.selectedField = databaseFields[0];
        this.setSelectableFields(this.databaseTables[0].id);
      }
    });
  }

  setDatabase(){
    this.setSelectableTables(this.selectedDatabase.id);
  }

  setSelectableTables(databaseId){
    this.selectableTables = [];
    this.databaseTables.forEach(
      databaseTable => {
        if(databaseTable.sheetName==null){
          databaseTable.sheetName = databaseTable.tableName;
          databaseTable.exportable = true;
        }
        if(databaseTable.databaseId==databaseId){
          this.selectableTables.push(databaseTable);
        }
      }
    );
    this.selectedTable=this.selectableTables[0];
    this.setSelectableFields(this.selectedTable.id);
  }

  setTable(){
    this.setSelectableFields(this.selectedTable.id);
  }

  setSelectableFields(databaseTableId){
    this.selectableFields = [];
    this.selectedFieldsDisplayed=[];
    this.databaseFields.forEach(
      databaseField => {
        if(databaseField.databaseTableId==databaseTableId){
          // resetea lista de campos seleccionables que se muestra en pantalla.
          this.selectableFields.push(databaseField);
          // Resetea lista de campos seleccionados que se muestra en pantalla.
          if(databaseField.selected){
            this.selectedFieldsDisplayed.push(databaseField);
          }
        }
      }
    );
    this.isDataLoaded = true;
  }

  addField(field){
    field.selected=true;
    this.selectedFieldsDisplayed.push(field);
    
    //Añade el campo a la lista de tablas
    console.log(this.selectedTable.databaseFields);
    if(this.selectedTable.databaseFields==null){
      this.selectedTable.databaseFields = [];
    }
    this.selectedTable.databaseFields.push(field);
    for(let databaseTable of this.databaseTables) {
      if(databaseTable.id==this.selectedTable.id){
        databaseTable=this.selectedTable;
        break;
      }
    }

    console.log(this.databaseTables);

    // Establece el campo como seleccionado.
    let foundIndex = this.databaseFields.findIndex(data => data.id === field.id);
    this.databaseFields[foundIndex].selected=true;
  }

  removeField(field){
    field.selected=false;
    let foundIndex = this.selectedFieldsDisplayed.findIndex(data => data.id === field.id);
    this.selectedFieldsDisplayed = this.selectedFieldsDisplayed.filter((_, index)=> index !== foundIndex);
    
    //Elimina el campo de la lista de tablas
    console.log(this.selectedTable);
    this.selectedTable.databaseFields = this.selectedTable.databaseFields.filter((_, index)=> index !== foundIndex);
    console.log(this.selectedTable);
    for(let databaseTable of this.databaseTables) {
      if(databaseTable.id==this.selectedTable.id){
        if(databaseTable.databaseFields.length==0){
          databaseTable.databaseFields=null;
        }
        databaseTable=this.selectedTable;
        break;
      }
    }
    // Establece el campo como no seleccionado.
    foundIndex = this.databaseFields.findIndex(data => data.id === field.id);
    this.databaseFields[foundIndex].selected=false;
  }

  gen() {
    this.processStatus=0;
    this.message = "";
    this.fileService.filename=this.filename;
    this.fileService.databasesToExport=this.databases;
    this.fileService.tablesToExport=this.databaseTables;
    this.fileService.fieldsToExport=this.databaseFields;
    console.log(this.fileService.databasesToExport);
    console.log(this.fileService.tablesToExport);
    console.log(this.fileService.fieldsToExport);
    this.fileService.genFile().subscribe(
      data => {
        console.log(data);
      },
      error => {
        this.message = JSON.parse(JSON.stringify(error.error.text))
        console.log(this.message+" "+this.message.includes("\\"));
        if(this.message.endsWith(".xlsx")){
          this.processStatus=2;
          this.fileService.saveFile(this.message);
          
          /* To open files without authorization
          window.open(this.message, "_blank");*/
        }
        else{
          this.processStatus=1;
        }
      });
  }

  getFile(filename:string){
    this.fileService.saveFile(filename);
  }

  listFilesIsEmpty(){
    if(this.databases!=null && this.databases.length>0){
      this.isListOfFilesEmpty=false;
      return;
    }
    this.isListOfFilesEmpty=true;
  }
}