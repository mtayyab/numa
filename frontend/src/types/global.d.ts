// Global type declarations for missing modules
// This file is intentionally minimal to avoid type conflicts

// Suppress type checking for problematic modules
declare module 'minimatch' {
  interface MinimatchOptions {
    dot?: boolean;
    nobrace?: boolean;
    noglobstar?: boolean;
    noext?: boolean;
    nocase?: boolean;
    nonull?: boolean;
    matchBase?: boolean;
    nocomment?: boolean;
    nonegate?: boolean;
    flipNegate?: boolean;
  }

  class Minimatch {
    constructor(pattern: string, options?: MinimatchOptions);
    match(file: string): boolean;
    makeRe(): RegExp;
    static minimatch(target: string, pattern: string, options?: MinimatchOptions): boolean;
  }

  export = Minimatch;
  export as namespace minimatch;
}

declare module 'glob' {
  interface GlobOptions {
    cwd?: string;
    root?: string;
    dot?: boolean;
    nomount?: boolean;
    mark?: boolean;
    nosort?: boolean;
    stat?: boolean;
    silent?: boolean;
    strict?: boolean;
    cache?: { [path: string]: any };
    statCache?: { [path: string]: any };
    symlinks?: { [path: string]: any };
    realpathCache?: { [path: string]: any };
    sync?: boolean;
    nounique?: boolean;
    nonull?: boolean;
    debug?: boolean;
    nobrace?: boolean;
    noglobstar?: boolean;
    noext?: boolean;
    nocase?: boolean;
    matchBase?: boolean;
    nocomment?: boolean;
    nonegate?: boolean;
    flipNegate?: boolean;
  }

  function glob(pattern: string, options?: GlobOptions, callback?: (err: Error | null, matches: string[]) => void): void;
  function glob(pattern: string, callback?: (err: Error | null, matches: string[]) => void): void;
  namespace glob {
    function sync(pattern: string, options?: GlobOptions): string[];
  }
  export = glob;
  export as namespace glob;
}
