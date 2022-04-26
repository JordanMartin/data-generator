export interface OutputConfig {
  count: number;
  format: string;
  include_null?: boolean;
  filename_template?: string;
  pretty?: boolean;
  object_name?: string;
  table_name?: string;
  template?: string;
  separator?: string;
  gzip?: boolean;
  single_file?: boolean;
}
