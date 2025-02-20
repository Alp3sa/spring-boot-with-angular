import { DatabaseField } from '../models/databaseField';

export class DatabaseTable {
	id: number;
    databaseId: number;
    tableName: string;
    databaseFields: DatabaseField[] = [];
    selected: boolean;
    exportable: boolean;
    sheetName: string;
    queryConditions: string;
}