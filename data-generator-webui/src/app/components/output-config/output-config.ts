export interface OutputConfig {
  count: number;
  count_per_file?: number;
  format: string;
  include_null?: boolean;
  filename_template?: string;
  pretty?: boolean;
  object_name?: string;
  table_name?: string;
  template?: string;
  separator?: string;
  gzip?: boolean;
  zip?: boolean;
}
