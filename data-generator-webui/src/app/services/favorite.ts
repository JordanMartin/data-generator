import {OutputConfig} from '../components/output-config/output-config';

export interface Favorite {
  name: string;
  definition: string;
  output_config: OutputConfig;
  create_date?: Date;
  update_date?: Date;
}
