export interface VersionInfo {
  version: string;
  latest: LatestVersion
}

export interface LatestVersion {
  version: string;
  date: string;
}
