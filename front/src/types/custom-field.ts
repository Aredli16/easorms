export interface CustomField {
  id?: string;
  name: string;
  type: CustomFieldType;
}

export enum CustomFieldType {
  TEXT = 'TEXT',
  NUMBER = 'NUMBER',
  DATE = 'DATE',
  BOOLEAN = 'BOOLEAN',
}
