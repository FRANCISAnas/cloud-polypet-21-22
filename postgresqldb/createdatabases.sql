
CREATE USER anas WITH PASSWORD 'anas_69';

CREATE DATABASE cloudpolypetbackenddb;
GRANT ALL PRIVILEGES ON DATABASE cloudpolypetbackenddb TO anas;

CREATE DATABASE cloudpolypetaccountingdb;
GRANT ALL PRIVILEGES ON DATABASE cloudpolypetaccountingdb TO anas;

CREATE DATABASE cloudpolypetcataloguedb;
GRANT ALL PRIVILEGES ON DATABASE cloudpolypetcataloguedb TO anas;

CREATE DATABASE cloudpolypetcustomercaredb;
GRANT ALL PRIVILEGES ON DATABASE cloudpolypetcustomercaredb TO anas;

CREATE DATABASE cloudpolypetsitemonitoringdb;
GRANT ALL PRIVILEGES ON DATABASE cloudpolypetsitemonitoringdb TO anas;

/*DO $$ DECLARE
   r RECORD;
BEGIN
   FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = current_schema()) LOOP
     EXECUTE 'DROP TABLE ' || quote_ident(r.tablename) || ' CASCADE';
   END LOOP;
END $$;*/