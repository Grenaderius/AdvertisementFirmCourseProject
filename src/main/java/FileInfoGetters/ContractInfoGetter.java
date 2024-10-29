package FileInfoGetters;

import Paths.Paths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ContractInfoGetter {
    private final String contractName;
    private final Paths paths = new Paths();

    public ContractInfoGetter(String contractName) {
        this.contractName = contractName;
    }

    public String getContractContent() throws IOException {
        String content;

        Path contractPath = Path.of(paths.getContractsPath() + contractName);
        content = Files.readString(contractPath);

        return content;
    }

}
