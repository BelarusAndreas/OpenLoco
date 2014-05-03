package openloco.datfiles;

public enum DescriptionType {
    objdata,		// -
    lang,		// langid
    useobj,		// num* type classes...
    auxdata,		// nameind numaux* size num* (regular auxdata, known number of entries)
    auxdatafix,	// nameind numaux* size numsize (fixed length auxdata, length stored before auxdata)
    auxdatavar,	// nameind numaux* size type (variable length auxdata, delimited by 0xFF*type)
    strtable,		// id numofs ofsofs
    cargo,		// num*
    sprites,		// -
    sounds,		// -

    // * num entries have these possibilities:
    //	num > 0 means fixed number (array support)
    //	num = 0 means fixed number, single entry (no array support)
    //	num < 0: num is an index into the objdata array where the
    //		 actual number of entries is stored (array support)
    //
    //	num + 0x01xx0000 mean until data[] == 0xff; only supported for desc_useobj
    //	num + 0x02xx0000 means use data[num] & xx
    //	num + 0x03000000 means use data[num] != 0 ? 1 : 0
    //	num + 0x04000000 means special calculation type in calcdescnum[] in objects.c

    END;
}
